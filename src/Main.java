import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Classe para representar um item do menu
    static class MenuItem {
        private int id;
        private String name;
        private double price;

        public MenuItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return String.format("%d. %s - R$ %.2f", id, name, price);
        }
    }

    // Classe para representar um item do pedido
    static class OrderItem {
        private MenuItem menuItem;
        private int quantity;

        public OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        public MenuItem getMenuItem() { return menuItem; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return menuItem.getPrice() * quantity; }

        @Override
        public String toString() {
            return String.format("%s x%d - R$ %.2f",
                    menuItem.getName(), quantity, getTotalPrice());
        }
    }

    enum UserOptions {
        ORDER(1), EXIT(2);

        private final int value;

        UserOptions(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

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
        MAIN_COURSE(1), DESSERT(2), DRINKS(3), FINISH_ORDER(0);

        private final int value;

        FoodOptions(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static FoodOptions fromValue(int value) {
            for (FoodOptions option : FoodOptions.values()) {
                if (option.getValue() == value) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Opção inválida: " + value);
        }
    }

    // Cardápio do restaurante
    private static final List<MenuItem> MAIN_COURSES = List.of(
            new MenuItem(1, "Filé à Parmegiana", 32.90),
            new MenuItem(2, "Salmão Grelhado", 45.50),
            new MenuItem(3, "Frango à Milanesa", 28.90),
            new MenuItem(4, "Lasanha Bolonhesa", 26.90)
    );

    private static final List<MenuItem> DESSERTS = List.of(
            new MenuItem(1, "Tiramisu", 15.90),
            new MenuItem(2, "Pudim de Leite", 12.50),
            new MenuItem(3, "Torta de Chocolate", 18.90),
            new MenuItem(4, "Sorvete", 8.90)
    );

    private static final List<MenuItem> DRINKS = List.of(
            new MenuItem(1, "Refrigerante", 6.50),
            new MenuItem(2, "Suco Natural", 8.90),
            new MenuItem(3, "Água", 3.50),
            new MenuItem(4, "Café", 4.50)
    );

    private static void printMainMenu() {
        System.out.println("Menu");
        System.out.println("1. Fazer Pedido");
        System.out.println("2. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void printFoodMenu() {
        System.out.println("\nCardápio:");
        System.out.println("1. Pratos Principais");
        System.out.println("2. Sobremesas");
        System.out.println("3. Bebidas");
        System.out.println("0. Finalizar Pedido");
        System.out.print("Escolha uma categoria: ");
    }

    private static void printMenuItems(List<MenuItem> items, String category) {
        System.out.println("\n" + category + ":");
        for (MenuItem item : items) {
            System.out.println(item);
        }
        System.out.print("Digite o número do item: ");
    }

    private static MenuItem findMenuItem(List<MenuItem> items, int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static void printReceipt(String customerName, List<OrderItem> orderItems,
                                     double serviceRate, double totalAmount) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           NOTA FISCAL");
        System.out.println("         Restaurante da Marina");
        System.out.println("=".repeat(40));
        System.out.println("Cliente: " + customerName);
        System.out.println("-".repeat(40));

        double subtotal = 0;
        for (OrderItem item : orderItems) {
            System.out.println(item);
            subtotal += item.getTotalPrice();
        }

        System.out.println("-".repeat(40));
        System.out.printf("Subtotal: R$ %.2f%n", subtotal);
        System.out.printf("Taxa de Serviço (%.0f%%): R$ %.2f%n",
                (serviceRate/subtotal) * 100, serviceRate);
        System.out.printf("TOTAL: R$ %.2f%n", totalAmount);
        System.out.println("=".repeat(40));
    }

    private static void processOrder(Scanner input) {
        input.nextLine(); // Limpar buffer

        System.out.print("Qual é o nome do cliente? ");
        String customerName = input.nextLine();

        List<OrderItem> orderItems = new ArrayList<>();
        boolean ordering = true;

        while (ordering) {
            printFoodMenu();
            try {
                int categoryChoice = input.nextInt();
                FoodOptions foodOption = FoodOptions.fromValue(categoryChoice);

                switch (foodOption) {
                    case MAIN_COURSE:
                        addItemToOrder(input, MAIN_COURSES, "Pratos Principais", orderItems);
                        break;
                    case DESSERT:
                        addItemToOrder(input, DESSERTS, "Sobremesas", orderItems);
                        break;
                    case DRINKS:
                        addItemToOrder(input, DRINKS, "Bebidas", orderItems);
                        break;
                    case FINISH_ORDER:
                        if (orderItems.isEmpty()) {
                            System.out.println("Não é possível finalizar um pedido vazio!");
                        } else {
                            ordering = false;
                            finalizeOrder(input, customerName, orderItems);
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("Opção inválida! Tente novamente.");
                input.nextLine(); // Limpar buffer em caso de erro
            }
        }
    }

    private static void addItemToOrder(Scanner input, List<MenuItem> menuItems,
                                       String category, List<OrderItem> orderItems) {
        printMenuItems(menuItems, category);
        try {
            int itemId = input.nextInt();
            MenuItem selectedItem = findMenuItem(menuItems, itemId);

            if (selectedItem != null) {
                System.out.print("Quantidade: ");
                int quantity = input.nextInt();

                if (quantity > 0) {
                    orderItems.add(new OrderItem(selectedItem, quantity));
                    System.out.printf("%s x%d adicionado ao pedido!%n",
                            selectedItem.getName(), quantity);
                } else {
                    System.out.println("Quantidade inválida!");
                }
            } else {
                System.out.println("Item não encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Entrada inválida!");
            input.nextLine(); // Limpar buffer
        }
    }

    private static void finalizeOrder(Scanner input, String customerName,
                                      List<OrderItem> orderItems) {
        // Calcular subtotal
        double subtotal = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        // Solicitar taxa de serviço
        System.out.printf("%nSubtotal do pedido: R$ %.2f%n", subtotal);
        System.out.print("Deseja adicionar taxa de serviço? (10% sugerido) [S/N]: ");
        input.nextLine(); // Limpar buffer
        String addService = input.nextLine().toUpperCase();

        double serviceRate = 0;
        if (addService.equals("S") || addService.equals("SIM")) {
            System.out.print("Digite a porcentagem da taxa de serviço (ex: 10): ");
            try {
                double percentage = input.nextDouble();
                serviceRate = subtotal * (percentage / 100);
            } catch (Exception e) {
                System.out.println("Valor inválido! Usando 10% por padrão.");
                serviceRate = subtotal * 0.10;
                input.nextLine(); // Limpar buffer
            }
        }

        double totalAmount = subtotal + serviceRate;

        // Exibir nota fiscal
        printReceipt(customerName, orderItems, serviceRate, totalAmount);

        // Processar pagamento
        System.out.printf("%nValor a pagar: R$ %.2f%n", totalAmount);
        System.out.print("Quanto foi recebido em dinheiro? R$ ");
        try {
            double amountReceived = input.nextDouble();
            if (amountReceived >= totalAmount) {
                double change = amountReceived - totalAmount;
                System.out.printf("Troco: R$ %.2f%n", change);
                System.out.println("Obrigado pela preferência!");
            } else {
                System.out.println("Valor insuficiente!");
            }
        } catch (Exception e) {
            System.out.println("Valor inválido!");
            input.nextLine(); // Limpar buffer
        }
    }

    public static void main(String[] args) {
        System.out.println("Restaurante da Marina\n");
        Scanner input = new Scanner(System.in);

        boolean running = true;

        while (running) {
            try {
                printMainMenu();
                int chosenOptionValue = input.nextInt();
                UserOptions chosenOption = UserOptions.fromValue(chosenOptionValue);

                switch(chosenOption) {
                    case ORDER:
                        processOrder(input);
                        break;
                    case EXIT:
                        System.out.println("Saindo do programa. Obrigado!");
                        running = false;
                        break;
                }

            } catch (Exception error) {
                System.out.println("Erro: " + error.getMessage());
                System.out.println("Tente novamente.\n");
                input.nextLine(); // Limpar buffer em caso de erro
            }
        }

        input.close();
    }
}