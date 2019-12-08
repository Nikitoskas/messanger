import server.security.service.TokenHandler;

public class Test {
    @org.junit.jupiter.api.Test
    public void test1(){
        TokenHandler tokenHandler = new TokenHandler();
        System.out.println(tokenHandler.createToken("test"));
    }
}
