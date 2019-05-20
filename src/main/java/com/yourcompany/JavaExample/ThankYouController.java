package com.yourcompany.JavaExample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import org.json.JSONObject;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ThankYouController {

  @PostMapping("/thankyou")
  public RedirectView thankyou(@RequestBody MultiValueMap<String, String> formParams) {

    Map<String, String> response = this.parse(formParams);

    if (response.get("success").equals("true")) {
      return new RedirectView("success");
    } else {
      return new RedirectView("failed");
    }
  }

  @GetMapping("/success")
  public String success(Model model) {
    model.addAttribute("name", "Success");
    return "success";
  }

  @GetMapping("/failed")
  public String failed(Model model) {
    model.addAttribute("name", "failed");
    return "failed";
  }

  private Map<String, String> parse(MultiValueMap<String, String> params) {

    Map<String, String> response = new HashMap<String, String>();

    if (params.getFirst("kount_response") != null) {
      byte[] byteDecoded = Base64.getDecoder().decode(params.getFirst("kount_response").toString().getBytes());
      String decodedString = new String(byteDecoded);

      JSONObject jsonObj = new JSONObject(decodedString);
      response.put("score", jsonObj.get("SCOR").toString());
    }

    String success = params.getFirst("transaction[events][0][success]");
    String processorMessage = params.getFirst("transaction[events][0][processor_message]");

    response.put("success", success);
    response.put("message", processorMessage);

    return response;
  }

}