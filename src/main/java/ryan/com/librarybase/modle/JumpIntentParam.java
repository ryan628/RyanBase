package ryan.com.librarybase.modle;

import java.io.Serializable;

/**
 * Created by ryanx on 2015/4/15.
 */
public class JumpIntentParam implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3965801301450867212L;

    /**
     *
     */
    public JumpIntentParam(String n, Object o) {
        this.pname = n;
        this.pvalue = o;
    }

    public String pname;
    public Object pvalue;

    /**
     * @return the pname
     */
    public String getPname() {
        return pname;
    }

    /**
     * @param pname the pname to set
     */
    public void setPname(String pname) {
        this.pname = pname;
    }

    /**
     * @return the pvalue
     */
    public Object getPvalue() {
        return pvalue;
    }

    /**
     * @param pvalue the pvalue to set
     */
    public void setPvalue(Object pvalue) {
        this.pvalue = pvalue;
    }
}
