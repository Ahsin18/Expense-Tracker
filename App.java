import javax.swing.*;
import java.awt.*;
import java.util.*;

// Chart imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;




public class App 
{
    //Data
  static double budget= 0;
  static ArrayList<Expense> expenses = new ArrayList<>();
   // Labels
   static JLabel budgetLabel = new JLabel("Budget: ₹0 ");
   static JLabel expenseLabel = new JLabel("Expenses: ₹0 ");
   static JLabel remainingLabel = new JLabel("Balance: ₹0 ");
   static JLabel percentLabel = new JLabel("Percent Spent: 0% ");


   static JPanel chartContainer = new JPanel(new BorderLayout());

   static class Expense
   {
    double amount;
    String category;

    Expense(double amount, String category)
    {
        this.amount = amount;
        this.category = category;
    }
   }
 
public static void main (String ahsin[])
{
    JFrame frame= new JFrame("Expense Tracker");
    frame.setSize(700,500);
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setBackground(new Color(35,35,35));
    
    JTextField budgetField = new JTextField();
    JTextField amountField = new JTextField();
    JTextField categoryField = new JTextField();

    JButton setBudgetbtn = new JButton("Set Budget");
    JButton addExpensebtn = new JButton("Add Expense");
    JButton chartbtn = new JButton("Show Chart");
     Font btnFont = new Font("Arial", Font.BOLD, 14);
     setBudgetbtn.setFont(btnFont);
     addExpensebtn.setFont(btnFont);
     chartbtn.setFont(btnFont);
   
    //Panel for inputs
     JPanel mainPanel = new JPanel(new GridLayout(1,2,20,20));
     mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
     mainPanel.setBackground(new Color(35,35,35));

     JPanel leftPanel = new JPanel(new GridLayout(8,1,10,10));
     leftPanel.setBackground(new Color(45,45,45));
    leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Add Details",
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));
     JLabel bLabel = new JLabel("Budget:");
     JLabel aLabel = new JLabel("Amount:");
     JLabel cLabel = new JLabel("Category:");
     Font labelFont = new Font("Arial", Font.BOLD, 14);

     bLabel.setForeground(Color.WHITE);
     aLabel.setForeground(Color.WHITE);
     cLabel.setForeground(Color.WHITE); 

    leftPanel.add(bLabel);
    leftPanel.add(budgetField);
    leftPanel.add(setBudgetbtn);

    leftPanel.add(aLabel);
    leftPanel.add(amountField);

    leftPanel.add(cLabel);
    leftPanel.add(categoryField);
    leftPanel.add(addExpensebtn);


    //Right 
    JPanel rightPanel = new JPanel(new BorderLayout(10,10));
    rightPanel.setBackground(new Color(45,45,45));
    rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Summary",
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE
        ));

    JPanel summaryTop = new JPanel(new GridLayout(4,1,5,5));
    summaryTop.setBackground(new Color(45,45,45));
    Font summaryFont = new Font("Arial", Font.BOLD, 14);
     budgetLabel.setFont(summaryFont);
     expenseLabel.setFont(summaryFont);
     remainingLabel.setFont(summaryFont);
     percentLabel.setFont(summaryFont);

    budgetLabel.setForeground(Color.WHITE);
    expenseLabel.setForeground(Color.WHITE);
    remainingLabel.setForeground(Color.WHITE);
    percentLabel.setForeground(Color.WHITE);

    summaryTop.add(budgetLabel);
    summaryTop.add(expenseLabel);
    summaryTop.add(remainingLabel);
    summaryTop.add(percentLabel);


    chartContainer.setBackground(new Color(45,45,45));

        rightPanel.add(summaryTop, BorderLayout.NORTH);
        rightPanel.add(chartContainer, BorderLayout.CENTER);
        rightPanel.add(chartbtn, BorderLayout.SOUTH);

        // ADD BOTH PANELS
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

         setBudgetbtn.setBackground(new Color(70,130,180));
        setBudgetbtn.setForeground(Color.WHITE);

        addExpensebtn.setBackground(new Color(60,179,113));
        addExpensebtn.setForeground(Color.WHITE);

        chartbtn.setBackground(new Color(255,140,0));
        chartbtn.setForeground(Color.WHITE);
         setBudgetbtn.addActionListener(e -> {
            try {
                budget = Double.parseDouble(budgetField.getText());
                budgetField.setText("");
                updateSummary();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid budget!");
            }
        });

        addExpensebtn.addActionListener(e -> {
            try {
                if (budget == 0) {
                    JOptionPane.showMessageDialog(frame, "Set budget first!");
                    return;
                }

                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                expenses.add(new Expense(amount, category));

                amountField.setText("");
                categoryField.setText("");

                updateSummary();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid expense!");
            }
        });
        chartbtn.addActionListener(e -> {

            if (expenses.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No expenses!");
                return;
            }

            HashMap<String, Double> map = new HashMap<>();

            for (Expense exp : expenses) {
                map.put(exp.category,
                        map.getOrDefault(exp.category, 0.0) + exp.amount);
            }

            DefaultPieDataset dataset = new DefaultPieDataset();

            for (String key : map.keySet()) {
                dataset.setValue(key, map.get(key));
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Expense Summary",
                    dataset,
                    true,
                    true,
                    false
            );
            chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
            chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));

           ChartPanel chartPanel = new ChartPanel(chart);

            chartContainer.removeAll();
            chartContainer.add(chartPanel, BorderLayout.CENTER);
            chartContainer.revalidate();
            chartContainer.repaint();
        });
        frame.setVisible(true);
    }

    static void updateSummary() {

        double total = 0;

        for (Expense exp : expenses) {
            total += exp.amount;
        }

        double remaining = budget - total;
        double percent = (budget > 0) ? (total / budget) * 100 : 0;

        budgetLabel.setText("Budget: ₹" + budget);
        expenseLabel.setText("Expenses: ₹" + total);
        remainingLabel.setText("Balance: ₹" + remaining);
        percentLabel.setText("Percent Spent: " + String.format("%.2f", percent) + "%");

        if (total > budget) {
            JOptionPane.showMessageDialog(null, "⚠ You exceeded budget!");
        }
    }
}