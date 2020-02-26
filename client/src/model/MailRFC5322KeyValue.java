package model;

public class MailRFC5322KeyValue
{

    private String _key;
    private String _value;

    public MailRFC5322KeyValue(String key, String value)
    {
        this._key = key;
        this._value = value;
    }

    public MailRFC5322KeyValue(String value)
    {
        this._value = value;
        this._key = "MESSAGE";
    }

    public String getKey()
    {
        return _key;
    }

    public void setKey(String key)
    {
        this._key = key;
    }

    public String getValue()
    {
        return _value;
    }

    public void setValue(String value)
    {
        this._value = value;
    }

    @Override
    public String toString()
    {
        return this._key + " "  + this._value;
    }
}
