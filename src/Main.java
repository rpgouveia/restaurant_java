import java.util.Scanner;

public class Main {

    enum UserOptions {
        // Opções
        ORDER(1), EXIT(2);

        // atributo
        private final int value;

        // construtor
        UserOptions(int value) {
            this.value = value;
        }

        // getter
        public int getValue() {
            return value;
        }

        // método
        public static UserOptions fromValue(int value) {
            for (UserOptions option : UserOptions.values()) {
                if (option.getValue() == value) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Opção inválida: " + value);
        }
    }

    enum FoodOptions {
        // Opções
        MAIN_COURSE(1), DESSERT(2), DRINKS(3);

        // atributo
        private final int value;

        // construtor
        FoodOptions(int value) {
            this.value = value;
        }

        // getter
        public int getValue() {
            return value;
        }

        // método
        public static FoodOptions fromValue(int value) {
            for (FoodOptions option : FoodOptions.values()) {
                if (option.getValue() == value) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Opção inválida: " + value);
        }
    }

    private static void print_main_menu() {
        System.out.println("Menu");
        System.out.println("1. Fazer Pedido");
        System.out.println("2. Sair");
        System.out.println("Escolha uma opção: ");
    }

    public static void main(String[] args) {
        System.out.println("Restaurante da Marina\n");
        Scanner input = new Scanner(System.in);

        try {
            print_main_menu();
            int chosenOptionValue = input.nextInt();
            UserOptions chosenOption = UserOptions.fromValue(chosenOptionValue);

            switch(chosenOption) {
                case ORDER:
                    System.out.println("\nQual é o nome do cliente?\n");
                    break;
                case EXIT:
                    System.out.println("Saindo do programa.\n");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } catch (Exception error) {
            System.out.println("Erro: " + error.getMessage());
        } finally {
            input.close();
        }
    }

}
