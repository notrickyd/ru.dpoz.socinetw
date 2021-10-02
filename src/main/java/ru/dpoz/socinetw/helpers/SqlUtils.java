package ru.dpoz.socinetw.helpers;

import java.text.MessageFormat;

public class SqlUtils
{
    private static char escapeChar = '\\';

    public static String prepareParam(String param)
    {
        return param.replace("\\", MessageFormat.format("{0}\\", escapeChar))
                .replace("%", MessageFormat.format("{0}%", escapeChar))
                .replace("_", MessageFormat.format("{0}_", escapeChar));
                //.replace("[", MessageFormat.format("{0}[", escapeChar));
    }
}
