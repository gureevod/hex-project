import core.api.RestClient;
import org.junit.jupiter.api.Test;
import project.model.Brand;

public class test1 {

    @Test
    void first() {
        Brand[] brands = RestClient.get("https://api.practicesoftwaretesting.com/brands", RestClient.jsonConfig()).thenReturn().as(Brand[].class);
    }
}
