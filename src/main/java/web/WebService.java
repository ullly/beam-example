package web;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.util.Random;

public class WebService implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        Random generator = new Random();
        int toss = generator.nextInt(1000);
        response.getWriter().write(toss + "\n");
    }
}
