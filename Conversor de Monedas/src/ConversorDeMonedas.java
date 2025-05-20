import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorDeMonedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Real brasileño => Dólar");
            System.out.println("4) Dólar => Real brasileño");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Salir");
            System.out.print("Elija una opción válida: ");
            opcion = scanner.nextInt();

            if (opcion >= 1 && opcion <= 6) {
                System.out.print("Ingrese el valor que desea convertir: ");
                double amount = scanner.nextDouble();

                String from = "", to = "";

                switch (opcion) {
                    case 1 -> { from = "USD"; to = "ARS"; }
                    case 2 -> { from = "ARS"; to = "USD"; }
                    case 3 -> { from = "BRL"; to = "USD"; }
                    case 4 -> { from = "USD"; to = "BRL"; }
                    case 5 -> { from = "USD"; to = "COP"; }
                    case 6 -> { from = "COP"; to = "USD"; }
                }

                convertirMoneda(from, to, amount);
            }
        } while (opcion != 7);

        System.out.println("¡Gracias por usar el conversor de monedas!");
        scanner.close();
    }

    public static void convertirMoneda(String from, String to, double amount) {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/777600ccf0b093cdb4547821/latest/" + from;

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }
            in.close();

            JSONObject obj = new JSONObject(responseContent.toString());
            if (obj.getString("result").equals("success")) {
                double rate = obj.getJSONObject("conversion_rates").getDouble(to);
                double resultado = amount * rate;
                System.out.printf("%.2f %s son %.2f %s\n", amount, from, resultado, to);
            } else {
                System.out.println("Error al obtener los datos: " + obj.toString());
            }

        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
        }
    }
}
