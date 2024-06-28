import java.sql.*;
import java.util.Scanner;

public class SistemaGerenciamentoPedidos {

    private static final String URL = "jdbc:postgresql://localhost:5432/banco_de_dados";
    private static final String USUARIO = "seu_login";
    private static final String SENHA = "sua_senha";

    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conectarAoBanco();

            if (autenticarUsuario()) {
                exibirPrimeiroMenu();
                int opcao;

                do {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer do scanner

                    switch (opcao) {
                        case 1:
                            adicionarCliente();
                            break;
                        case 2:
                            listarClientes();
                            break;
                        case 3:
                            atualizarCliente();
                            break;
                        case 4:
                            excluirCliente();
                            break;
                        // Adicionar outras opções do menu
                        case 0:
                            System.out.println("Saindo do sistema...");
                            break;
                        default:
                            System.out.println("Opção não correspondente. Por favor, escolha outra vez.");
                    }
                } while (opcao != 0);
            } else {
                System.out.println("Usuário ou senha incorretos. Saindo do serviço.");
            }

            desconectarDoBanco();
        } catch (SQLException e) {
            System.err.println("Houve um problema ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private static void conectarAoBanco() {
        try {
            connection = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida.");
        } catch (SQLException e) {
            System.err.println("problema ao conectar ao banco de dados: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void desconectarDoBanco() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexão com o banco de dados chegada ao fim.");
            }
        } catch (SQLException e) {
            System.err.println("Problema ao encerrar conexão com o banco de dados: " + e.getMessage());
        }
    }

    private static boolean autenticarUsuario() {
        System.out.print("Por favor, insira seu nome de login: ");
        String usuario = scanner.nextLine();
        System.out.print("Por favor, insira sua senha: ");
        String senha = scanner.nextLine();

        return usuario.equals("admin") && senha.equals("admin123");
    }

    private static void exibirMenuPrincipal() {
        System.out.println("===== Bem vindo ao Serviço de Gerenciamento de Pedidos de Restaurante =====");
        System.out.println("Por favor, escolha um dos modos :");
        System.out.println("1. Adicionar um novo cliente");
        System.out.println("2. Listar todos os clientes");
        System.out.println("3. Atualizar informações de um cliente");
        System.out.println("4. Excluir um cliente");
        System.out.println("0. Sair do sistema");
        System.out.print("Sua escolha: ");
    }

    private static void adicionarCliente() {
        try {
            System.out.println("===== Adicionar Novo cliente =====");
            System.out.print("Por favor, insira o nome do cliente: ");
            String nome = scanner.nextLine();
            System.out.print("Por favor, insira o telefone do cliente: ");
            String telefone = scanner.nextLine();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO clientes (nome, telefone) VALUES (?, ?)");
            statement.setString(1, nome);
            statement.setString(2, telefone);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cliente adicionado com sucesso!");
            } else {
                System.out.println("Houve um problema ao adicionar o cliente. Por favor, tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar cliente: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        try {
            System.out.println("===== Lista de Clientes =====");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes");
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                System.out.println("Nome: " + nome + ", Telefone: " + telefone);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
    }

    private static void atualizarCliente() {
        try {
            System.out.println("===== Atualizar Informações do Cliente =====");
            System.out.print("Por favor, insira o nome do cliente que deseja atualizar: ");
            String nomeAntigo = scanner.nextLine();
            System.out.print("Por favor, insira o novo nome do cliente: ");
            String novoNome = scanner.nextLine();
            System.out.print("Por favor, insira o novo telefone do cliente: ");
            String novoTelefone = scanner.nextLine();

            PreparedStatement statement = connection.prepareStatement("UPDATE clientes SET nome = ?, telefone = ? WHERE nome = ?");
            statement.setString(1, novoNome);
            statement.setString(2, novoTelefone);
            statement.setString(3, nomeAntigo);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Informações do cliente atualizadas com sucesso!");
            } else {
                System.out.println("Houve um problema ao atualizar as informações do cliente. Verifique o nome e tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    private static void excluirCliente() {
        try {
            System.out.println("===== Excluir Cliente =====");
            System.out.print("Por favor, insira o nome do cliente que deseja excluir: ");
            String nomeCliente = scanner.nextLine();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM clientes WHERE nome = ?");
            statement.setString(1, nomeCliente);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cliente excluído com sucesso!");
            } else {
                System.out.println("Houve um problema ao excluir o cliente. Verifique o nome e tente novamente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}
