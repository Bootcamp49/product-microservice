package com.nttdata.bootcamp.productmanagement.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Clase utilitaria para los tests.
 */
public class Util {
    /**
     * Método para leer un archivo tipo JSON.
     * @param <T> Clase generica.
     * @param jsonFile Archivo de tipo JSON
     * @param clase Tipo del objeto JSON a leer.
     */
    @SuppressWarnings("unchecked")
    public <T> T serializeArchive(String jsonFile, Class<?> clase) {
        Gson gson = new Gson();
        T response = null;
        String bit;
        String json = "";
        File file = new File("src/test/resources/json/" + jsonFile);
        try (
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
                );
            ) {
            while ((bit = input.readLine()) != null) {
                json += bit;
            }
            json = new String(json.getBytes(ConstantsUtil.defaulEncodingApi), 
            ConstantsUtil.defaulEncodingApi);
            InputStream stream = new ByteArrayInputStream(json.getBytes());
            response = (T) gson.fromJson(new InputStreamReader(stream), clase);
        } catch (Exception e) {
            System.out.println("Error - Test/Util: " + e.getMessage());
        }
        return response;
    }
}
