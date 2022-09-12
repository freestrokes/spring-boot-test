package com.freestrokes;

import com.freestrokes.domain.Book;
import com.freestrokes.service.BookRestService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(BookRestService.class)
public class BookRestTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private BookRestService bookRestService;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    public void restTest() {
        this.mockRestServiceServer
            .expect(requestTo("/rest/test"))
            .andRespond(withSuccess(new ClassPathResource("/test.json", getClass()), MediaType.APPLICATION_JSON));

        Book book = this.bookRestService.getRestBookList();
        assertThat(book.getTitle()).isEqualTo("테스트");
    }

    @Test
    public void restErrorTest() {
        this.mockRestServiceServer
            .expect(requestTo("/rest/test"))
            .andRespond(withServerError());

        this.thrown.expect(HttpServerErrorException.class);
        this.bookRestService.getRestBookList();
    }

}
