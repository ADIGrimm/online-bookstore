package online.bookstore.category;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import online.bookstore.dto.category.CategoryDto;
import online.bookstore.dto.category.CreateCategoryRequestDto;
import online.bookstore.security.JwtAuthenticationFilter;
import online.bookstore.security.JwtUtil;
import online.bookstore.service.impl.CategoryServiceImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @Mock
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/create-some-categories.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/delete-all-categories.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCategories_ShouldReturnPaginatedCategories() throws Exception {
        List<CategoryDto> expectedCategories = createListOfCategoryDto();
        Pageable pageable = PageRequest.of(0, 3);
        Page<CategoryDto> categoryPage = new PageImpl<>(
                expectedCategories,
                pageable,
                expectedCategories.size()
        );
        when(categoryService.getAll(pageable)).thenReturn(categoryPage);

        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        List<CategoryDto> actualCategories = objectMapper.readValue(
                objectMapper.readTree(jsonResponse).get("content").toString(),
                new TypeReference<List<CategoryDto>>() {}
        );

        Assertions.assertEquals(expectedCategories.size(), actualCategories.size());
        for (int i = 0; i < expectedCategories.size(); i++) {
            CategoryDto expectedCategory = expectedCategories.get(i);
            CategoryDto actualCategory = actualCategories.get(i);
            EqualsBuilder.reflectionEquals(expectedCategory, actualCategory);
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getById_WithValidId_ShouldReturnCategoryDto() throws Exception {
        Long categoryId = 1L;
        CategoryDto expected = createListOfCategoryDto().getFirst();
        when(categoryService.getById(categoryId)).thenReturn(expected);
        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actual = objectMapper.readValue(jsonResponse, CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/categories/delete-new-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createCategory() throws Exception {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();

        CategoryDto expected = new CategoryDto();
        expected.setName(requestDto.getName());
        expected.setDescription(requestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                CategoryDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    private List<CategoryDto> createListOfCategoryDto() {
        List<CategoryDto> categories = new ArrayList<>();
        categories.add(new CategoryDto()
                .setId(1L)
                .setName("Fiction")
                .setDescription("Fictional books"));
        categories.add(new CategoryDto()
                .setId(2L)
                .setName("Science")
                .setDescription("Scientific books"));
        categories.add(new CategoryDto()
                .setId(3L)
                .setName("History")
                .setDescription("Historical books"));
        return categories;
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Detective");
        requestDto.setDescription("There is a mystery that will be solved");
        return requestDto;
    }
}
