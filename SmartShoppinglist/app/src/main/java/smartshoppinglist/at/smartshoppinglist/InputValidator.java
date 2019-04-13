package smartshoppinglist.at.smartshoppinglist;

public class InputValidator {

    public static boolean validInputString(String string){
            if(string == null || string.length() == 0 ) return false;
            return true;
    }
    public static boolean validInputString(String string, int length){
        if(string == null || string.length() == 0  || string.length() > length) return false;
        return true;
    }
    public static boolean validInputEmptyString(String string){
        if(string == null) return false;
        return true;
    }
    public static boolean validInputEmptyString(String string, int length){
        if(string == null || string.length() > length) return false;
        return true;
    }
    public static boolean validInputNumberString(String string, int length){
        if(string == null || string.length() == 0  || string.length() > length) return false;
        try{
            Double d = Double.parseDouble(string);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
