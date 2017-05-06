<#if packageName??>package ${packageName};</#if>

public class DomainFactories {

    protected DomainFactories() { }

    <#list domains as domain>
        <#if !domain.abstract>
            <#assign className = domain.name?cap_first>

            /** Creates a new ${domain.name}. */
            public static ${className} new${className}() {
                return new ${className}();
            }
        </#if>
    </#list>

}
