package org.example;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.InvocationTargetException;

public class InjectorTest {

    Injector injector = new Injector();

    @BeforeEach
    void createInjector() {
        injector = new Injector();
    }

    @Test
    @DisplayName("Test simple injection")
    public void testSimpleInjection() throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
            injector.register(HttpService.class, DarkWebHttpService.class);
            injector.register(NewsService.class, RssNewsService.class);

            NewsService newsService = (NewsService) injector.newInstance(NewsService.class);
            assertTrue(newsService.getHttpService() instanceof DarkWebHttpService);

    }
}

