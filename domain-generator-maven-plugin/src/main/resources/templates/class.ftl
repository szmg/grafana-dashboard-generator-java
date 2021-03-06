<#--
 #%L
 domain-generator-maven-plugin
 %%
 Copyright (C) 2017 Mate Gabor Szvoboda
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->
<#if packageName??>package ${packageName};</#if>

import java.util.List;
import java.util.Map;
import java.util.Set;

<#assign className = domain.name?cap_first>
<#assign withReturnType = className>
<#assign parent = domain.extendedClass!"BaseJsonObject">
<#if domain.abstract>
    <#assign withReturnType = "C">
    public abstract class ${className}<C extends BaseJsonObject<C>> extends ${parent}<C> {
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

    /**
      * Gets ${field.name}.
      * ${field.description!?html}
      *
      * @return ${field.name}
      */
    public ${field.type} get${fieldNameCap}() {
        return getValue(${fieldNameConst});
    }

    /**
      * Sets ${field.name}.
      * ${field.description!?html}
      *
      * @param ${field.name} the ${field.name}
      */
    public void set${fieldNameCap}(${field.type} ${field.name}) {
        setValue(${fieldNameConst}, ${field.name});
    }

    /**
      * Sets ${field.name}.
      * ${field.description!?html}
      *
      * @param ${field.name} the ${field.name}
      * @return itself
      */
    public ${withReturnType} with${fieldNameCap}(${field.type} ${field.name}) {
        return withValue(${fieldNameConst}, ${field.name});
    }

    <#if field.type?starts_with("List<")>
        <#assign innerType = field.type?keep_after("<")?keep_before_last(">")>
        <#assign singularName = fieldNameCap?remove_ending("s")>
        <#assign paramName = singularName?uncap_first>

        /**
          * Adds a(n) ${field.name?remove_ending("s")}.
          * ${field.description!?html}
          *
          * @param ${paramName} the ${paramName}
          * @return itself
          */
        public ${withReturnType} add${singularName}(${innerType} ${paramName}) {
            ${field.type} _list = get${fieldNameCap}();
            if (_list == null) {
                _list = new java.util.ArrayList<>();
            }
            _list.add(${paramName});
            return with${fieldNameCap}(_list);
        }
    </#if>
</#list>

<#if domain.abstract>
    /** Generic implementation of ${className}. It's easier to to use sometimes. */
    public static class Generic extends ${className}<Generic> { }
</#if>

}


<#function toConstName name>
    <#return "FIELD_" + name?replace("[A-Z]", "_$0", "r")?upper_case>
</#function>
