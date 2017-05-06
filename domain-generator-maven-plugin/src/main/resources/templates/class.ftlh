<#if packageName??>package ${packageName};</#if>

import java.util.List;
import java.util.Map;
import java.util.Set;

<#assign className = domain.name?cap_first>
<#assign withReturnType = className>
<#assign parent = domain.extendedClass!"BaseJsonObject">
<#if domain.abstract>
    <#assign withReturnType = "C">
    public class ${className}<C extends BaseJsonObject<C>> extends ${parent}<C> {
<#else>
    public class ${className} extends ${parent}<${className}> {
</#if>


<#list domain.fields as field>
    protected static final String ${toConstName(field.name)} = "${field.name?j_string}";
</#list>


public ${className}() {
    super();
<#if domain.defaultValues??>
    <#list domain.defaultValues?keys as field>
        setValue("${field?j_string}", ${domain.defaultValues[field]});
    </#list>
</#if>
}


<#list domain.fields as field>
    <#assign fieldNameConst = toConstName(field.name)>
    <#assign fieldNameCap = field.name?cap_first>
    <#assign javadoc>
        <#if field.description??>
            /**
              * ${field.description}
              */
        </#if>
    </#assign>

    ${javadoc}
    public ${field.type} get${fieldNameCap}() {
        return getValue(${fieldNameConst});
    }

    ${javadoc}
    public void set${fieldNameCap}(${field.type} ${field.name}) {
        setValue(${fieldNameConst}, ${field.name});
    }

    ${javadoc}
    public ${withReturnType} with${fieldNameCap}(${field.type} ${field.name}) {
        return withValue(${fieldNameConst}, ${field.name});
    }
</#list>


}


<#function toConstName name>
    <#return "FIELD_" + name?replace("[A-Z]", "_$0", "r")?upper_case>
</#function>
