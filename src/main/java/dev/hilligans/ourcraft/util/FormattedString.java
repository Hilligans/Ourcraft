package dev.hilligans.ourcraft.util;

import java.util.List;

public class FormattedString {

    /*public static StringTemplate.Processor<String, RuntimeException> FSTR = FormattedString::interpolate;

    private static String interpolate(StringTemplate stringTemplate) {
        StringBuilder builder = new StringBuilder();
        List<String> fragments = stringTemplate.fragments();
        List<Object> values = stringTemplate.values();
        Object[] newValues = new Object[values.size()];
        for(int x = 0; x < fragments.size()-1; x++) {
            String fragment = fragments.get(x);
            if(!hasFormatter(fragment)) {
                fragment += "%s";
                newValues[x] = String.valueOf(values.get(x));
            } else {
                newValues[x] = values.get(x);
            }
            builder.append(fragment);
        }
        builder.append(fragments.getLast());
        return String.format(builder.toString(), newValues);
    }

    private static boolean hasFormatter(String f) {
        for(int x = 0; x < f.length() - 1; x++) {
            if(f.charAt(x) == '%' && f.charAt(x+1) == '%') {
                x++;
                continue;
            }
            if(f.charAt(x) == '%') {
                return true;
            }
        }
        return false;
    } */
}
