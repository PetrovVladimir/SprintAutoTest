package logic.testcase.events;

import models.EventParams;
import models.webpackage.Element;

import javax.swing.*;
import java.util.Optional;

public class UserInputEvent extends EventWrapper {
    private Element element;

    public UserInputEvent(EventParams params, Element element) {
        super(params);
        this.element = element;
    }

    @Override
    protected void doWork(){
        System.out.println("        ввод пользовательского значения для элемента " + Optional.ofNullable(element.getName()).orElse(element.getLocator()));
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        String userValue = JOptionPane.showInputDialog(dialog, "Введите значение для элемента " + element.getName());

        seleniumServise().click(element);
        for (Character ch : userValue.toCharArray()) {
            seleniumServise().pressKey(ch.toString());
        }
    }
}
