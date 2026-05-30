package com.mycompany.proyectocv.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.json.JSONObject;

public class BarcodeLookupService {

    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static ProductoLookup buscar(String codigo) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + codigo + ".json"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() != 200) {
                return new ProductoLookup("", false);
            }

            JSONObject json = new JSONObject(res.body());
            JSONObject product = json.optJSONObject("product");

            if (product != null) {
                String nombre = product.optString("product_name", "");
                if (!nombre.isEmpty()) {
                    return new ProductoLookup(nombre, true);
                }
            }

            return new ProductoLookup("", false);

        } catch (Exception e) {
            System.err.println("Error al consultar API: " + e.getMessage());
            return new ProductoLookup("", false);
        }
    }
}
