import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MoedaConverter {

    private static final String API_KEY = "SUA_CHAVE_DE_API_AQUI"; // Substitua pela sua chave de API
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/58f8919390881ee13ae940e2/latest/USD";

    public static void main(String[] args) {
        try {
            // Chama o método para pegar a taxa de câmbio
            JsonObject taxas = obterTaxasDeCambio();

            // Exibe as opções de moedas
            String[] moedas = {"BRL", "EUR", "GBP", "JPY", "AUD", "CAD"};
            System.out.println("Escolha a moeda para conversão:");
            for (int i = 0; i < moedas.length; i++) {
                System.out.println((i + 1) + ". " + moedas[i]);
            }

            // Lê a escolha do usuário
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite o número da moeda que deseja converter: ");
            int escolha = scanner.nextInt();

            // Valida a entrada do usuário
            if (escolha < 1 || escolha > moedas.length) {
                System.out.println("Escolha inválida.");
                return;
            }

            String moedaEscolhida = moedas[escolha - 1];

            // Solicita o valor a ser convertido
            System.out.print("Digite o valor em USD para conversão: ");
            double valorEmDolar = scanner.nextDouble();

            // Converte o valor para a moeda escolhida
            double valorConvertido = converterMoeda(taxas, valorEmDolar, moedaEscolhida);
            System.out.println("Valor convertido para " + moedaEscolhida + ": " + valorConvertido);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject obterTaxasDeCambio() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream());
             Scanner scanner = new Scanner(reader)) {

            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

            // Usando GSON para desserializar a resposta JSON
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            return jsonResponse.getAsJsonObject("conversion_rates");
        }
    }

    public static double converterMoeda(JsonObject taxas, double valor, String moeda) {
        double taxa = taxas.get(moeda).getAsDouble();
        return valor * taxa;
    }
}
