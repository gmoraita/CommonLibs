package net.allwebdesign.common.lib.utils;

import java.util.logging.Logger;



/**
 * Converts an amount to litteral
 * @author George Moraitakis
 *
 */
public class AmountToLiteral {

    private static String[] m   = { "", "ема ", "дуо ", "тяиа ",
                                    "тессеяа ", "пемте ", "ени ",
                                    "епта ", "ойты ", "еммеа " };
    private static String[] mF  = { "", "лиа ", "", "тяеис ",
                                    "тессеяис " };
    private static String[] d1  = { //дИАЖОЯОПОъГСЕИР ТЫМ 11,12 ...
                                    "дейа ", "емтейа ",
                                    "дыдейа " };
    private static String[] d   = { "", "дейа", "еийоси ",
                                    "тяиамта ", "саяамта ",
                                    "пемгмта ", "енгмта ",
                                    "ебдолгмта ", "оцдомта ",
                                    "емемгмта " };
    private static String[] e   = { "", "ейато", "диайоси",
                                    "тяиайоси", "тетяайоси",
                                    "пемтайоси", "енайоси",
                                    "ептайоси", "ойтайоси",
                                    "еммиайоси" };
    private static String[] idx = { "кепта", "еуяы ", "викиадес ",
                                    "ейатоллуяи", "дис", "тяис",
                                    "тетяайис ", "пемтайис " };

    private static double round(double value)
    {
        return Math.round(value);
    }

    /**
     * Gets a literal of a string amount. By default it shows the zeroes and the currency
     * Be careful not to include currency symbols or non numericals
     * @param money the money to convert
     * @param isGreekFormat special preprocessing (for greek removes . and replaces , with .)
     * @return the literal format
     */
    public static String getLiteralFromString(String money, boolean isGreekFormat)
    {
    	if (isGreekFormat){
    		money = money.replace(".", "");
    		money = money.replace(",", ".");
    	} else{
    		money = money.replace(",", "");
    	}
    	double moneyDouble;
    	try{
    		moneyDouble = Double.parseDouble(money);
    	}catch (Exception e){
    		Logger.getAnonymousLogger().severe("Cannot convert amount to literal. It contains invalid characters");
    		return money;
    	}
    	
        return getLiteral(moneyDouble);
    }
    
    /**
     * Gets a literal of an amount. By default it shows the zeroes and the currency
     * @param money the money to convert
     * @return the literal format
     */
    public static String getLiteral(double money)
    {
        return getLiteral(money, true);
    }

    /**
     * Gets a literal of an amount and turns on/off the display of zeroes. 
     * By default it shows the currency 
     * @param money the money to convert
     * @param showZero display or not the zeroes
     * @return the literal format
     */
    public static String getLiteral(double money, boolean showZero)
    {
        return getLiteral(money, showZero, true);
    }

    /**
     * Gets a literal of an amount and turns on/off the display of zeroes and the currency. 
     * @param money he money to convert
     * @param showZero display or not the zeroes
     * @param showCurrency display or not the currency
     * @return
     */
    public static String getLiteral(double money, boolean showZero,
                                   boolean showCurrency )
    {
        String str;
        short index = 0;
        boolean isZero = true;
        boolean isNegative = false;

        str = "";

        if (money < 0)
        {
            money = -money;
            isNegative = true;
        }

        if (money != (long)money)
        {
            short value = (short)round(100 * money
                                - 100 * Math.floor(money));
            if (value >= 100)
            {
                value -= 100;
                money += 1.0;
            }

            money = (long)money;
            if (value > 0)
            {
                isZero = false;

                if (money >= 1 && value > 0)
                {
                    str += "йаи ";
                }
                str += GetValue(value, index, showCurrency);
            }
        }

        while (money >= 1)
        {
            isZero = false;
            short value = (short)((long)money % 1000);
            money /= 1000;
            index += 1;
            str = GetValue(value, index, showCurrency) + str;
            money = (long)money;
        }

        if (isZero)
        {
            if (showZero)
            {
                str = "лгдем ";
                if (showCurrency)
                {
                    str += idx[1];
                }
            }
        }
        else
        {
            if (isNegative){
                str = "MEION " + str;
        	}
        }

        return str;
    }

    private static String GetValue( short money, short index,
                            boolean showCurrency)
    {
        if (index == 2 && money == 1)
        {
            return "викиа ";
        }

        String str = "";
        int dekmon = money % 100;
        int monades = dekmon % 10;
        int ekatontades = (int)(money / 100);
        int dekades = (int)(dekmon / 10);

        //EKATONTADES
        if (ekatontades == 1)
        {
            if (dekmon == 0)
            {
                str = e[1] + " ";
            }
            else
            {
                str = e[1] + "м ";
            }
        }
        else if (ekatontades > 1)
        {
            if (index == 2)
            {
                str = e[ekatontades] + "ес ";
            }
            else
            {
                str = e[ekatontades] + "а ";
            }
        }

        //DEKADES
        switch (dekmon)
        {
            case 10:
                str += d1[monades];	//"дейа " ЛЕ ЙЕМЭ СТО ТщКОР
                break;
            case 11:
                str += d1[monades];
                monades = 0;
                break;
            case 12:
                str += d1[monades];
                monades = 0;
                break;
            default:
                str += d[dekades];
                break;
        }

        //MONADES
        if ((index == 2) &&
            (monades == 1 || monades == 3 || monades == 4))
        {
            str += mF[monades];
        }
        else
        {
            if (dekmon < 10 || dekmon > 12)
            {
                str += m[monades];
            }
        }

        if (str.length() > 0 || index == 1)
        {
            if (index == 0 && money == 1)
            {
                if (showCurrency)
                {
                    str += "кепто";
                }
            }
            else
            {
                if (index > 1 || showCurrency)
                {
                    str += idx[index];
                    if (index > 2)
                    {
                        if (index > 3)
                        {
                            str += idx[3];
                        }
                        if (money > 1)
                        {
                            str += "а ";
                        }
                        else
                        {
                            str += "о ";
                        }
                    }
                }
            }
        }

        return str;
    }

} //class Money