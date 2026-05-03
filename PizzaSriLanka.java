import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ===================== ENUMS =====================

enum PizzaType {
    VEG, NON_VEG
}

enum PizzaSize {
    SMALL, MEDIUM, LARGE
}

enum Crust {
    THIN, REGULAR, SECRET
}

enum PaymentMethod {
    CASH, CARD
}

enum OrderStatus {
    NEW, PLACED, PREPARING, READY, COMPLETED
}

// ===================== INTERFACE =====================

interface Sellable {
    String getId();

    String getName();

    double getPrice();
}

// ===================== ABSTRACT PARENT CLASS =====================

abstract class MenuItem implements Sellable {
    private String id;
    private String name;
    private double price;

    public MenuItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        setPrice(price);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            this.price = 0;
        }
    }

    @Override
    public abstract String toString();
}

// ===================== CHILD CLASSES =====================

class Pizza extends MenuItem {
    private PizzaType type;
    private PizzaSize size;
    private Crust crust;

    public Pizza(String id, String name, double price, PizzaType type, PizzaSize size, Crust crust) {
        super(id, name, price);
        this.type = type;
        this.size = size;
        this.crust = crust;
    }

    public PizzaType getType() {
        return type;
    }

    public PizzaSize getSize() {
        return size;
    }

    public Crust getCrust() {
        return crust;
    }

    @Override
    public String toString() {
        return String.format(
                "%-6s | %-7s | %-25s | %-8s | %-6s | %-7s | Rs. %.2f",
                getId(), "Pizza", getName(), type, size, crust, getPrice());
    }
}

class Side extends MenuItem {
    private String category;

    public Side(String id, String name, double price, String category) {
        super(id, name, price);
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format(
                "%-6s | %-7s | %-25s | %-8s | %-6s | %-7s | Rs. %.2f",
                getId(), "Side", getName(), category, "-", "-", getPrice());
    }
}

class Drink extends MenuItem {
    private String volume;

    public Drink(String id, String name, double price, String volume) {
        super(id, name, price);
        this.volume = volume;
    }

    @Override
    public String toString() {
        return String.format(
                "%-6s | %-7s | %-25s | %-8s | %-6s | %-7s | Rs. %.2f",
                getId(), "Drink", getName(), volume, "-", "-", getPrice());
    }
}

// ===================== ORDER ITEM =====================

class OrderItem {
    private MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;

        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            this.quantity = 1;
        }
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "%-25s x %-3d = Rs. %.2f",
                item.getName(), quantity, getSubtotal());
    }
}

// ===================== ORDER =====================

class Order {
    private static int orderCounter = 1001;

    private String orderId;
    private String customerName;
    private List<OrderItem> items;
    private String specialInstructions;
    private OrderStatus status;
    private PaymentMethod paymentMethod;

    public Order(String customerName) {
        this.orderId = "ORD" + orderCounter++;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.specialInstructions = "None";
        this.status = OrderStatus.NEW;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(MenuItem item) {
        addItem(item, 1);
    }

    public void addItem(MenuItem item, int quantity) {
        if (item != null && quantity > 0) {
            items.add(new OrderItem(item, quantity));
        }
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void setSpecialInstructions(String notes) {
        if (notes != null && !notes.trim().isEmpty()) {
            this.specialInstructions = notes;
        }
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethod = method;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double calculateSubtotal() {
        double subtotal = 0;

        for (OrderItem item : items) {
            subtotal += item.getSubtotal();
        }

        return subtotal;
    }

    public double calculateDiscount() {
        double subtotal = calculateSubtotal();

        if (subtotal >= 5000) {
            return subtotal * 0.10;
        } else if (subtotal >= 3000) {
            return subtotal * 0.05;
        }

        return 0;
    }

    public double calculateFinalTotal() {
        return calculateSubtotal() - calculateDiscount();
    }

    public void displaySummary() {
        System.out.println("\n==================== ORDER SUMMARY ====================");
        System.out.println("Order ID             : " + orderId);
        System.out.println("Customer Name        : " + customerName);
        System.out.println("Order Status         : " + status);
        System.out.println("Payment Method       : " + paymentMethod);
        System.out.println("Special Instructions : " + specialInstructions);
        System.out.println("-------------------------------------------------------");

        for (OrderItem item : items) {
            System.out.println(item);
        }

        System.out.println("-------------------------------------------------------");
        System.out.printf("Subtotal             : Rs. %.2f%n", calculateSubtotal());
        System.out.printf("Discount             : Rs. %.2f%n", calculateDiscount());
        System.out.printf("Final Total          : Rs. %.2f%n", calculateFinalTotal());
        System.out.println("=======================================================");
    }
}

// ===================== MENU =====================

class Menu {
    private List<MenuItem> itemList;
    private Map<String, MenuItem> itemMap;

    public Menu() {
        this.itemList = new ArrayList<>();
        this.itemMap = new HashMap<>();
    }

    public void addItem(MenuItem item) {
        itemList.add(item);
        itemMap.put(item.getId().toUpperCase(), item);
    }

    public boolean removeItem(String id) {
        MenuItem item = itemMap.remove(id.toUpperCase());

        if (item != null) {
            itemList.remove(item);
            return true;
        }

        return false;
    }

    public MenuItem getItemById(String id) {
        return itemMap.get(id.toUpperCase());
    }

    public void displayMenu() {
        System.out.println("\n============================== MENU ==============================");
        System.out.printf(
                "%-6s | %-7s | %-25s | %-8s | %-6s | %-7s | %s%n",
                "ID", "Item", "Name", "Type/Info", "Size", "Crust", "Price");
        System.out.println("------------------------------------------------------------------");

        if (itemList.isEmpty()) {
            System.out.println("No menu items available.");
        } else {
            for (MenuItem item : itemList) {
                System.out.println(item);
            }
        }

        System.out.println("==================================================================");
    }

    public void search(String query) {
        boolean found = false;

        System.out.println("\nSearch results for: " + query);
        System.out.println("------------------------------------------------------------------");

        for (MenuItem item : itemList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                System.out.println(item);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching items found.");
        }
    }

    public void sortByPriceAscending() {
        itemList.sort(Comparator.comparingDouble(MenuItem::getPrice));
    }

    public void sortByPriceDescending() {
        itemList.sort(Comparator.comparingDouble(MenuItem::getPrice).reversed());
    }

    public void sortByName() {
        itemList.sort(Comparator.comparing(MenuItem::getName));
    }
}

// ===================== MAIN APPLICATION =====================

public class PizzaSriLanka {
    private static Menu menu = new Menu();
    private static List<Order> allOrders = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int idGen = 1;

    public static void main(String[] args) {
        seedData();

        boolean running = true;

        while (running) {
            displayMainMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addMenuItemUI();
                    break;

                case 2:
                    removeMenuItemUI();
                    break;

                case 3:
                    menu.displayMenu();
                    break;

                case 4:
                    searchSortUI();
                    break;

                case 5:
                    placeOrderUI();
                    break;

                case 6:
                    viewAllOrders();
                    break;

                case 7:
                    updateOrderStatusUI();
                    break;

                case 8:
                    System.out.println("Thank you for using Pizza Sri Lanka System.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1 to 8.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n================ PIZZA SRI LANKA SYSTEM ================");
        System.out.println("1. Manager: Add Menu Item");
        System.out.println("2. Manager: Remove Menu Item");
        System.out.println("3. View Menu");
        System.out.println("4. Search / Sort Menu");
        System.out.println("5. Place Order");
        System.out.println("6. View All Orders");
        System.out.println("7. Manager: Update Order Status");
        System.out.println("8. Exit");
        System.out.println("========================================================");
    }

    private static void updateOrderStatusUI() {
        if (allOrders.isEmpty()) {
            System.out.println("No orders available to update.");
            return;
        }

        System.out.print("Enter Order ID to update (e.g., ORD1001): ");
        String searchId = scanner.nextLine();

        Order targetOrder = null;
        for (Order o : allOrders) {
            if (o.getOrderId().equalsIgnoreCase(searchId)) {
                targetOrder = o;
                break;
            }
        }

        if (targetOrder != null) {
            System.out.println("Current status: " + targetOrder.getStatus());
            System.out.println("Select new status: ");
            System.out.println("1. PLACED | 2. PREPARING | 3. READY | 4. COMPLETED");
            int sChoice = readInt("Select status (1-4): ");

            switch (sChoice) {
                case 1:
                    targetOrder.setStatus(OrderStatus.PLACED);
                    System.out.println("Status updated successfully.");
                    break;
                case 2:
                    targetOrder.setStatus(OrderStatus.PREPARING);
                    System.out.println("Status updated successfully.");
                    break;
                case 3:
                    targetOrder.setStatus(OrderStatus.READY);
                    System.out.println("Status updated successfully.");
                    break;
                case 4:
                    targetOrder.setStatus(OrderStatus.COMPLETED);
                    System.out.println("Status updated successfully.");
                    break;
                default:
                    System.out.println("Invalid selection. Status not changed.");
            }
        } else {
            System.out.println("Error: Order ID not found.");
        }
    }

    private static String generateItemId() {
        return "I" + String.format("%03d", idGen++);
    }

    private static void seedData() {
        menu.addItem(
                new Pizza(generateItemId(), "Margherita Pizza", 1200, PizzaType.VEG, PizzaSize.MEDIUM, Crust.REGULAR));
        menu.addItem(new Pizza(generateItemId(), "Chicken Supreme Pizza", 2500, PizzaType.NON_VEG, PizzaSize.LARGE,
                Crust.THIN));
        menu.addItem(new Pizza(generateItemId(), "Veggie Delight Pizza", 1800, PizzaType.VEG, PizzaSize.LARGE,
                Crust.SECRET));

        menu.addItem(new Side(generateItemId(), "Garlic Bread", 450, "Appetizer"));
        menu.addItem(new Side(generateItemId(), "Chicken Wings", 950, "Side"));

        menu.addItem(new Drink(generateItemId(), "Coca Cola", 250, "500ml"));
        menu.addItem(new Drink(generateItemId(), "Bottled Water", 150, "500ml"));
    }

    private static void addMenuItemUI() {
        System.out.println("\n----------- ADD MENU ITEM -----------");
        System.out.println("1. Add Pizza");
        System.out.println("2. Add Side");
        System.out.println("3. Add Drink");

        int itemType = readInt("Choose item type: ");

        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        double price = readDouble("Enter price: Rs. ");

        if (itemType == 1) {
            PizzaType pizzaType = choosePizzaType();
            PizzaSize pizzaSize = choosePizzaSize();
            Crust crust = chooseCrust();

            menu.addItem(new Pizza(generateItemId(), name, price, pizzaType, pizzaSize, crust));
            System.out.println("Pizza added successfully.");

        } else if (itemType == 2) {
            System.out.print("Enter side category, for example Appetizer/Wings/Bread: ");
            String category = scanner.nextLine();

            menu.addItem(new Side(generateItemId(), name, price, category));
            System.out.println("Side added successfully.");

        } else if (itemType == 3) {
            System.out.print("Enter drink volume, for example 500ml/1L: ");
            String volume = scanner.nextLine();

            menu.addItem(new Drink(generateItemId(), name, price, volume));
            System.out.println("Drink added successfully.");

        } else {
            System.out.println("Invalid item type. Item was not added.");
        }
    }

    private static void removeMenuItemUI() {
        menu.displayMenu();

        System.out.print("Enter item ID to remove: ");
        String id = scanner.nextLine();

        if (menu.removeItem(id)) {
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Error: Invalid item ID.");
        }
    }

    private static void searchSortUI() {
        System.out.println("\n----------- SEARCH / SORT MENU -----------");
        System.out.println("1. Search by name");
        System.out.println("2. Sort by price: Low to High");
        System.out.println("3. Sort by price: High to Low");
        System.out.println("4. Sort by name: A to Z");

        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1:
                System.out.print("Enter search keyword: ");
                String query = scanner.nextLine();
                menu.search(query);
                break;

            case 2:
                menu.sortByPriceAscending();
                System.out.println("Menu sorted by price: Low to High.");
                menu.displayMenu();
                break;

            case 3:
                menu.sortByPriceDescending();
                System.out.println("Menu sorted by price: High to Low.");
                menu.displayMenu();
                break;

            case 4:
                menu.sortByName();
                System.out.println("Menu sorted by name: A to Z.");
                menu.displayMenu();
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void placeOrderUI() {
        System.out.println("\n----------- PLACE ORDER -----------");

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        Order order = new Order(customerName);

        boolean addingItems = true;

        while (addingItems) {
            menu.displayMenu();

            System.out.print("Enter Item ID to add, or type DONE to finish: ");
            String id = scanner.nextLine();

            if (id.equalsIgnoreCase("DONE")) {
                addingItems = false;
            } else {
                MenuItem item = menu.getItemById(id);

                if (item == null) {
                    System.out.println("Invalid item ID. Please try again.");
                } else {
                    int quantity = readInt("Enter quantity: ");

                    if (quantity > 0) {
                        order.addItem(item, quantity);
                        System.out.println(item.getName() + " added to order.");
                    } else {
                        System.out.println("Quantity must be greater than 0.");
                    }
                }
            }
        }

        if (!order.hasItems()) {
            System.out.println("Order cancelled. No items were added.");
            return;
        }

        System.out.print("Enter special instructions, or press Enter for none: ");
        String instructions = scanner.nextLine();
        order.setSpecialInstructions(instructions);

        PaymentMethod paymentMethod = choosePaymentMethod();
        order.setPaymentMethod(paymentMethod);

        order.setStatus(OrderStatus.PLACED);
        allOrders.add(order);

        order.displaySummary();
        System.out.println("Order placed successfully.");
    }

    private static void viewAllOrders() {
        System.out.println("\n==================== ALL ORDERS ====================");

        if (allOrders.isEmpty()) {
            System.out.println("No orders have been placed yet.");
        } else {
            for (Order order : allOrders) {
                order.displaySummary();
            }
        }
    }

    private static PizzaType choosePizzaType() {
        while (true) {
            System.out.println("\nChoose Pizza Type:");
            System.out.println("1. VEG");
            System.out.println("2. NON_VEG");

            int choice = readInt("Enter choice: ");

            if (choice == 1) {
                return PizzaType.VEG;
            } else if (choice == 2) {
                return PizzaType.NON_VEG;
            } else {
                System.out.println("Invalid pizza type.");
            }
        }
    }

    private static PizzaSize choosePizzaSize() {
        while (true) {
            System.out.println("\nChoose Pizza Size:");
            System.out.println("1. SMALL");
            System.out.println("2. MEDIUM");
            System.out.println("3. LARGE");

            int choice = readInt("Enter choice: ");

            if (choice == 1) {
                return PizzaSize.SMALL;
            } else if (choice == 2) {
                return PizzaSize.MEDIUM;
            } else if (choice == 3) {
                return PizzaSize.LARGE;
            } else {
                System.out.println("Invalid pizza size.");
            }
        }
    }

    private static Crust chooseCrust() {
        while (true) {
            System.out.println("\nChoose Crust:");
            System.out.println("1. THIN");
            System.out.println("2. REGULAR");
            System.out.println("3. SECRET");

            int choice = readInt("Enter choice: ");

            if (choice == 1) {
                return Crust.THIN;
            } else if (choice == 2) {
                return Crust.REGULAR;
            } else if (choice == 3) {
                return Crust.SECRET;
            } else {
                System.out.println("Invalid crust type.");
            }
        }
    }

    private static PaymentMethod choosePaymentMethod() {
        while (true) {
            System.out.println("\nChoose Payment Method:");
            System.out.println("1. CASH");
            System.out.println("2. CARD");

            int choice = readInt("Enter choice: ");

            if (choice == 1) {
                return PaymentMethod.CASH;
            } else if (choice == 2) {
                return PaymentMethod.CARD;
            } else {
                System.out.println("Invalid payment method.");
            }
        }
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(scanner.nextLine());

                if (value >= 0) {
                    return value;
                } else {
                    System.out.println("Price cannot be negative.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}