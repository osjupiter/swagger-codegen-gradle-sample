import io.swagger.codegen.*;
import io.swagger.codegen.languages.*;

class MySwaggerLogic extends JavaClientCodegen {
    @Override
    public void updateCodegenPropertyEnum(CodegenProperty var) {
        super.updateCodegenPropertyEnum(var)
        if(var.vendorExtensions.containsKey("x-enum-names")){
            def alternames=var.vendorExtensions['x-enum-names']
            def enums=var.allowableValues["enumVars"]
            if(alternames.size()!=enums.size()){
                return
            }
            alternames.eachWithIndex{str,i->
                enums[i]["name"]=str
            }
        }
    }
}