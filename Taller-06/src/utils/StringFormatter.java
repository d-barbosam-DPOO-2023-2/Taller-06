package utils;

public final class StringFormatter {
    public enum Alignment { LEFT, MID, RIGHT }
    /**
     * Pads a given string with a specified character to a specified length and alignment.
     *
     * @param string     The original string to be padded.
     * @param fillChar   The character used for padding.
     * @param length     The desired length of the resulting string.
     * @param alignment  The alignment for the padding (LEFT, MID, or RIGHT).
     * @return           The padded string.
     */
    public static String padding(String string, char fillChar, int length, Alignment alignment) {
        if (string.length() >= length)
            return string;
        String fill = null;
        if (alignment == Alignment.LEFT || alignment ==  Alignment.RIGHT)
            fill = String.valueOf(fillChar).repeat(length - string.length());
        else
            length -= string.length();
        return switch (alignment) {
            case LEFT -> string + fill;
            case MID -> String.valueOf(fillChar).repeat(length / 2) + string + String.valueOf(fillChar).repeat(length - length / 2);
            case RIGHT -> fill + string;
        };
    }

    /**
     * Fills an array of strings with a specified character to achieve a desired total length.
     *
     * @param strings    An array of strings to be filled and combined.
     * @param fillChar   The character used for filling the gaps between strings.
     * @param length     The desired total length of the resulting string.
     * @return           The combined string with gaps filled.
     */
    public static String fill(String[] strings, char fillChar, int length) {
        int totalStringLength = 0;
        for (String string : strings)
            totalStringLength += string.length();

        int gaps = strings.length - 1;
        int totalFillChars = length - totalStringLength;
        int charsPerGap = gaps > 0 ? totalFillChars / gaps : 0;
        int remainingChars = gaps > 0 ? totalFillChars % gaps : totalFillChars;

        StringBuilder result = new StringBuilder(strings[0]);

        for (int i = 1; i < strings.length; i++) {
            int fillChars = charsPerGap + (remainingChars > 0 ? 1 : 0);
            remainingChars--;
            result.append(String.valueOf(fillChar).repeat(fillChars)).append(strings[i]);
        }

        return result.toString();
    }
    /**
     * Fills an array of strings with a specified character while maintaining custom gap lengths
     * to achieve a desired total length.
     * If the last gap is -1, the last word will accommodate to the edge
     *
     * @param strings    An array of strings to be filled and combined.
     * @param fillChar   The character used for filling the gaps between strings.
     * @param length     The desired total length of the resulting string.
     * @param gaps       An array specifying the custom gap lengths between strings.
     * @return           The combined string with custom gaps filled.
     * @throws IllegalArgumentException If 'strings' and 'gaps' have different sizes;
     *                                  if gaps are not in ascending order; if gaps overlap
     *                                  or if gaps exceed the desired length.
     */
    public static String fill(String[] strings, char fillChar, int length, int ...gaps) {
        if (strings.length != gaps.length)
            throw new IllegalArgumentException("'strings' and 'gaps' must have the same size");
        StringBuilder builder = new StringBuilder();
        if (gaps[gaps.length - 1] == -1)
            gaps[gaps.length - 1] = length - strings[strings.length - 1].length();
        int previousGap = -1;
        int actualGap = 0;
        for (int index = 0; index < gaps.length; index ++) {
            if (gaps[index] <= previousGap)
                throw new IllegalArgumentException("Each gap must be greater than the previous one");
            int gap = gaps[index] - actualGap;
            if (gap < 0)
                throw new IllegalArgumentException("gaps cannot overlap strings");
            builder.append(String.valueOf(fillChar).repeat(gap));
            builder.append(strings[index]);
            actualGap = builder.length();
            previousGap = gaps[index];
        }
        if (builder.length() > length)
            throw new IllegalArgumentException("gaps cannot overflow strings");
        else if (builder.length() < length)
            builder.append(String.valueOf(fillChar).repeat(length - actualGap));
        return builder.toString();
    }

    /**
     * Wraps a given message to fit within the specified width without breaking words.
     *
     * @param message The message to wrap.
     * @param width   The maximum width for each line.
     * @return A wrapped version of the message.
     */
    public static String wrap(String message, int width) {
        if (message.length() <= width)
            return message;
        String[] words = message.split(" ");
        StringBuilder result = new StringBuilder();
        int currentLineLength = 0;
        for (String word : words)
            if (currentLineLength + word.length() <= width) {
                result.append(word).append(' ');
                currentLineLength += word.length() + 1;
            } else {
                result.append('\n').append(word).append(' ');
                currentLineLength = word.length() + 1;
            }
        return result.toString();
    }
}
