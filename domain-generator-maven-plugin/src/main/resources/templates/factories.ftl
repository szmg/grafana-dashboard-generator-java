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
