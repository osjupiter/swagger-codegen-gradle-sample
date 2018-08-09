@Grab('io.swagger:swagger-codegen-cli:2.3.1')

import io.swagger.codegen.*;
import io.swagger.codegen.languages.*;

class MyJaxrsClientGen extends JavaJerseyServerCodegen {

    @Override
    public void updateCodegenPropertyEnum(CodegenProperty var) {
        if(!updateCodegenPropertyEnumByVendor(var)){
            super.updateCodegenPropertyEnum(var)
        }
    }

    public boolean updateCodegenPropertyEnumByVendor(CodegenProperty var){
         Map<String, Object> allowableValues = var.allowableValues;

        // handle ArrayProperty
        if (var.items != null) {
            allowableValues = var.items.allowableValues;
        }

        if (allowableValues == null) {
            return false;
        }

        List<Object> values2 = (List<Object>) allowableValues.get("values");
        if (values2 == null) {
            return false;
        }

        if(!var.vendorExtensions.containsKey("x-enum-values")){
            return false
        }
        
        Object extension = var.vendorExtensions.get("x-enum-values");
        List<Map<String, Object>> values =
            (List<Map<String, Object>>) extension;
        
        if(values.size()!=values2.size()){
            return false
        }

        // put "enumVars" map into `allowableValues", including `name` and `value`
        List<Map<String, String>> enumVars = new ArrayList<Map<String, String>>();

        for (int i=0;i<values.size();i++) {
            String name = camelize((String) values[i].get("identifier"), true);
            Object originValue=values2[i]
            Map<String, String> enumVar = new HashMap<String, String>();
            if (isReservedWord(name)) {
                name = escapeReservedWord(name);
            }
            enumVar.put("name", toEnumVarName(name, var.datatype));
            enumVar.put("value", toEnumValue(originValue.toString(), var.datatype));
            enumVars.add(enumVar);
        }
        allowableValues.put("enumVars", enumVars);

        // handle default value for enum, e.g. available => StatusEnum.AVAILABLE
        if (var.defaultValue != null) {
            String enumName = null;
            for (Map<String, String> enumVar : enumVars) {
                if (toEnumValue(var.defaultValue, var.datatype).equals(enumVar.get("value"))) {
                    enumName = enumVar.get("name");
                    break;
                }
            }
            if (enumName != null) {
                var.defaultValue = toEnumDefaultValue(enumName, var.datatypeWithEnum);
            }
        }
        return true;

    }

 // CLIへののkick
  public static main(String[] args) {
    SwaggerCodegen.main(args)
  }

}
