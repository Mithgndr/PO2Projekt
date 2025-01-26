package ProjektPO2.Users;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomArrayList<E> extends ArrayList<E> {
    @Override
    public String toString() {
        String separator = ";";
        return this.stream()
                .map(Object::toString)
                .collect(Collectors.joining(separator));
    }
}