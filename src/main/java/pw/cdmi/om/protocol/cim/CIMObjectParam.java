package pw.cdmi.om.protocol.cim;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.collect.ComparisonChain;

@Entity
@Table(name = "CIM_ObjectParam")
public class CIMObjectParam implements Comparable<CIMObjectParam> {

    @Column(length = 24)
    private String type;			// CIM子类：如SMIS

    @Column(length = 64)
    private String objectName;		// MO名字

    @Column(length = 64)
    private String paramName;		// MO下属性名

    @Column(length = 128)
    private String handler;			// 默认为空，直接附值；有申明Handler时，通过handler处理再传值

    @Column(length = 64)
    private String orObject;		// OR映射对应的类

    @Column(length = 64)
    private String orParam;			// OR映射对应的属性

    public CIMObjectParam(String type, String objectName, String paramName, String handler, String orObject,
        String orParam) {
        super();
        this.type = type;
        this.objectName = objectName;
        this.paramName = paramName;
        this.handler = handler;
        this.orObject = orObject;
        this.orParam = orParam;
    }

    /**
     * OR类名（全路径）
     * @return
     */
    public String getOrObject() {
        return orObject;
    }

    public void setOrObject(String orObject) {
        this.orObject = orObject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 命令名
     * @return
     */
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        objectName = objectName;
    }

    /**
     * 命令名生成结果的参数名
     * 多种形式：
     * 1、直接为命令输出属性
     * 2、Map类型的KEY
     * 3、LIST包含的类型
     * @return
     */
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * 获取值处理类路径
     * @return
     */
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * 获取OR对象属性名
     * @return
     */
    public String getOrParam() {
        return orParam;
    }

    public void setOrParam(String orParam) {
        this.orParam = orParam;
    }

    @Override
    public int compareTo(CIMObjectParam o) {
        return ComparisonChain.start().compare(this.type, o.type).compare(this.objectName, o.objectName)
            .compare(this.paramName, o.paramName).result();
    }

    public CIMObjectParam(String type, String objectName, String paramName) {
        super();
        this.type = type;
        this.objectName = objectName;
        this.paramName = paramName;
    }

    public CIMObjectParam(String type, String objectName, String paramName, String handler, String orParam) {
        super();
        this.type = type;
        this.objectName = objectName;
        this.paramName = paramName;
        this.handler = handler;
        this.orParam = orParam;
    }

    public CIMObjectParam() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
        result = prime * result + ((paramName == null) ? 0 : paramName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CIMObjectParam other = (CIMObjectParam) obj;
        if (objectName == null) {
            if (other.objectName != null)
                return false;
        } else if (!objectName.equals(other.objectName))
            return false;
        if (paramName == null) {
            if (other.paramName != null)
                return false;
        } else if (!paramName.equals(other.paramName))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
