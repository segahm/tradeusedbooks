package server;
import java.io.*;
import javax.servlet.*;

public class ParameterParser{

  private ServletRequest req;

  public ParameterParser(ServletRequest req){
    this.req = req;
  }

  public String getString(String name)
      throws ParameterNotFoundException{
    // Use getParameterValues() to avoid the once-deprecated getParameter()
    String[] values = req.getParameterValues(name);
    if (values == null)
      throw new ParameterNotFoundException(name + " not found");
    else if (values[0].trim().length() == 0)
      throw new ParameterNotFoundException(name + " was empty");
    else
      return values[0].trim();  // ignore multiple field values
  }

  public String getString(String name, 
  	String def){
    try {
    	return tools.htmlDecode(getString(name));
    }catch (Exception e){ 
    	return def;
    }
  }

  public boolean getBoolean(String name)
      throws ParameterNotFoundException{
    return new Boolean(getString(name)).booleanValue();
  }

  public boolean getBoolean(String name,
  	boolean def){
    try{
    	return getBoolean(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public byte getByte(String name)
      throws ParameterNotFoundException,
      NumberFormatException{
    return Byte.parseByte(getString(name));
  }

  public byte getByte(String name, byte def){
    try {
    	return getByte(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public char getChar(String name)
      throws ParameterNotFoundException{
    String param = getString(name);
    if (param.length() == 0)  // shouldn't be possible
      throw new ParameterNotFoundException(name + " is empty string");
    else
      return (param.charAt(0));
  }

  public char getChar(String name, char def){
    try {
    	return getChar(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public double getDouble(String name)
      throws ParameterNotFoundException,
      NumberFormatException{
    return new Double(getString(name)).doubleValue();
  }

  public double getDouble(String name,
  	double def){
    try{ 
    	return getDouble(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public float getFloat(String name)
      throws ParameterNotFoundException,
       NumberFormatException{
    return new Float(getString(name)).floatValue();
  }

  public float getFloat(String name,
  	float def){
    try {
    	return getFloat(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public int getInt(String name)
      throws ParameterNotFoundException,
      NumberFormatException{
    return Integer.parseInt(getString(name));
  }

  public int getInt(String name, int def){
    try{
    	return getInt(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public long getLongParameter(String name)
      throws ParameterNotFoundException,
      NumberFormatException{
    return Long.parseLong(getString(name));
  }

  public long getLongParameter(String name, long def){
    try{
    	return getLongParameter(name);
    }catch (Exception e){ 
    	return def;
    }
  }

  public short getShortParameter(String name)
      throws ParameterNotFoundException,
      NumberFormatException{
    return Short.parseShort(getString(name));
  }

  public short getShortParameter(String name,
  	short def){
    try { 
    	return getShortParameter(name);
    }catch (Exception e){ 
    	return def;
    }
  }
}