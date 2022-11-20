package com.cs253.p4.project4;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Stack;

/*
 * Name:Alistair Chambers
 * Write a program that takes as input a fully parenthesized , arithmetic expression and convert it to a binary expression  tree.
 * Your program should display the tree in some way and also print the value associated with the root.
 * For an additional challenge , allow the leaves to store variables of X1, X2 , X3 and so on,
 * which are initially 0 and which can be updated interactively by your program,
 * with the corresponding update in the printed value of the root of the expression tree.
 * */

public class Project4 extends Application {
    Pane drawField = new Pane();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //fxmlLoader.load()


        TextField tex = new TextField();
        Text tet = new Text();
        Button bt = new Button();
        bt.setText("Update");
        bt.setMaxHeight(10);

        BorderPane bp = new BorderPane();
        bp.setBottom(tet);
        bp.setTop(tex);
        bp.setRight(bt);

        Group group = new Group(bp, drawField);
        Scene scene = new Scene(group, 600, 600);
        stage.setTitle("Expression Tree");
        bt.setOnAction((ActionEvent event) -> {
            infix_to_postfix tes = new infix_to_postfix();
            System.out.println(tes.String_to_infix_queue(tex.getText()));
            System.out.println(tes.toPostfix2());
            tet.setText("Postfix: " + tes.toPostfix2());
            //Noder tr = new Noder(expressionTree(tes.toPostfix2()));
            drawNodeRecursive(100, 100, 100, 100, (expressionTree(tes.toPostfix2())));
            tex.clear();
            //Cannot reset text field input to original State
        });
        stage.setScene(scene);
        stage.show();
    }

    public static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }
    //uses a postfix to Expression tree
    //Could have used infix to postfix ???
    //Not enough time to implemwnt
    public static Noder expressionTree(String postfix) {
        //Refactor Self made Stack implementation
        Stack<Noder> st = new Stack<Noder>();
        Noder t1, t2, temp;

        for (int i = 0; i < postfix.length(); i++) {
            if (!isOperator(postfix.charAt(i))) {
                temp = new Noder(postfix.charAt(i));
                st.push(temp);
            } else {
                temp = new Noder(postfix.charAt(i));

                t1 = st.pop();
                t2 = st.pop();

                temp.left = t2;
                temp.right = t1;

                st.push(temp);
            }

        }
        temp = st.pop();
        return temp;
    }
    public void drawNodeRecursive(int x1, int y1, int x, int y, Noder node) {
        Line line = new Line(x1, y1 + 5, x, y);
        drawField.getChildren().add(line);
        Circle circle = new Circle(x, y, 15, Paint.valueOf("white"));
        drawField.getChildren().add(circle);
        Text txt = new Text(x - 3, y + 3, String.valueOf(node.value));
        drawField.getChildren().add(txt);
        if (node.left != null)
            drawNodeRecursive(x, y, x - (30), y + 30, node.left);
        if (node.right != null)
            drawNodeRecursive(x, y, x + (30), y + 30, node.right);
    }
    //testing different methods
    void tobtree(String in) {
        Stack2 st = new Stack2();
        Noder r;

        for (int i = 0; i < in.length(); i++) {
            if (infix_to_postfix.isSymbol(in.charAt(i))) {
                st.push(in.charAt(i));
            } else {
                Noder rig = new Noder(st.pop());
                Noder lef = new Noder(st.pop());
                r = new Noder(in.charAt(i));

            }
        }

    }
}

class Noder {
    public char value;


    public Noder right;
    public Noder left;

    Noder(char v) {
        this.value = v;
        this.left = null;
        this.right = null;

    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Noder getRight() {
        return right;
    }

    public void setRight(Noder right) {
        this.right = right;
    }

    public Noder getLeft() {
        return left;
    }

    public void setLeft(Noder left) {
        this.left = left;
    }
}

class infix_to_postfix {
    String infix;
    Stack2 operators = new Stack2();
    Stack2 value = new Stack2();
    Queue infix_queue = new Queue();
    Queue postfix_queue = new Queue();

    public static boolean isSymbol(char IN) {
        return priority(IN) <= 0;
    }

    public static int priority(char x) {
        if (x == '(' || x == ')')
            return 1;
        else if (x == '+' || x == '-')
            return 2;
        else if (x == '*' || x == '/' || x == '%')
            return 3;
        else return 0;
    }

    public void print() {
        while (postfix_queue.front != null) {
            System.out.print(postfix_queue.dequeue());

        }
    }

    public String String_to_infix_queue(String in) {
        int infix_length = String.valueOf(in).length();
        String chars = "";
        for (int v = 0; v < infix_length; v++) {
            infix_queue.enqueue(in.charAt(v));
            chars += in.charAt(v);
        }
        infix = chars;
        return chars;
    }

    public int precedence(char operator) {
        return switch (operator) {

            case ')', '(' -> 4;
            case 'ˆ' -> 3;
            case '*', '/' -> 2;
            case '+', '-' -> 1;
            default -> 0;
        };
    }

    public String toPostfix2() {
        Queue infix_queue2 = infix_queue;
        String post = "";
        char ispop;
        int type;
        while (!infix_queue2.isEmpty()) {
            char val = infix_queue2.dequeue();
            type = precedence(val);

            if (type == 0 && val != '(' && val != ')') {
                postfix_queue.enqueue(val);

            } else if (val == '(') {
                operators.push(val);
            } else if (val == ')') {
                while (operators.peek() != '(') {
                    postfix_queue.enqueue(operators.pop());
                }
                operators.pop();
            } else if (precedence(val) > 0) {
                while (!Stack2.isEmpty() && operators.peek() != '(' && precedence(val) <= precedence(operators.peek())) {
                    postfix_queue.enqueue(operators.pop());
                }
                operators.push(val);

            }
            //infix_queue2.dequeue();

        }
        while (!Stack2.isEmpty()) {
            postfix_queue.enqueue(operators.pop());
        }
        for (int r = 0; r < infix.length(); r++) {
            char get = infix.charAt(r);
            if (isSymbol(get)) {
                post += get;

            } else if (get == ')') {
                while ((ispop = value.pop()) != '(') {
                    post += ispop;
                    //postfix_queue.enqueue(String.valueOf(ispop));
                }
            } else {
                while (!Stack2.isEmpty() && get != '(' && priority(value.peek()) >= priority(get)) {
                    post += value.pop();
                }
                value.push(get);
            }
        }
        while (!Stack2.isEmpty()) {
            post += value.pop();
        }
        return post;
    }
}

class Stack2 {
    static StackNode root;
    static int count = 0;

    public static boolean isEmpty() {
        return root == null || count == 0;
    }

    public char pop() {
        char popped = ' ';
        if (root == null) {
            //System.out.println("Stack is Empty");
        } else {
            popped = root.data;
            root = root.next;
        }
        count--;
        return popped;
    }

    public void push(char data) {
        StackNode newNode = new StackNode(data);

        if (root == null) {
            root = newNode;
        } else {
            StackNode temp = root;
            root = newNode;
            newNode.next = temp;
        }
        count++;
    }

    public char peek() {
        if (root == null) {
            System.out.println("Stack is empty");
            return 0;
        } else {
            return root.data;
        }
    }

    public int size() {
        return count;
    }

    class StackNode {
        char data;
        StackNode next;

        StackNode(char data) {
            this.data = data;
        }
    }
}

class Queue {
    Qnode front, rear;
    int count = 0;
    public Queue() {
        this.front = this.rear = null;
    }

    void enqueue(char key) {
        Qnode temp = new Qnode(key);
        count++;
        if (this.rear == null) {
            this.front = this.rear = temp;
            return;
        }
        this.rear.next = temp;
        this.rear = temp;
    }

    char dequeue() {
        Qnode temp = this.front;
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return '!';
        } else {
            count--;
            this.front = this.front.next;
            if (this.front == null)
                this.rear = null;
            return temp.key;
        }
    }

    int size() {
        return count;
    }

    boolean isEmpty() {
        return (size() == 0);
    }

    char peek() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
        }
        return front.key;
    }

    class Qnode {
        Qnode next;
        char key;

        public Qnode(char key) {
            this.key = key;
            this.next = null;
        }
    }
}
