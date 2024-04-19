package app.view;


public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }
}
