/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.altMgr;

public class EmailAllowedCharacters {
    public static final char[] allowedCharactersArray = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean isAllowedCharacter(char character) {
        return character >= ' ' && character != '\u007f';
    }

    public static String filterAllowedCharacters(String input) {
        StringBuilder var1 = new StringBuilder();
        for (char var5 : input.toCharArray()) {
            if (!EmailAllowedCharacters.isAllowedCharacter(var5)) continue;
            var1.append(var5);
        }
        return var1.toString();
    }
}

