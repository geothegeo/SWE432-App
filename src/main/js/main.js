var variables = "1";
var i;
var formGroup, inputLabel, selectLabel, inputName, varName;
var predicate = "";

    window.onload = function() {
  startUp();
  selectInputs();
  showUserInputs(variables);
}

function startUp() {
  var bgColor = document.getElementById("outlineBg");
  bgColor.value = "#ffffff";
  bgColor.addEventListener("input", updateColor, false);
}
          
function updateColor(event) {
  document.getElementById("predicateForm").setAttribute("style", "background-color: " + event.target.value + ";");
}

function selectInputs() {
     var inputNum = document.getElementById("inputNum"); 
  variables = inputNum.value;
  inputNum.addEventListener("change", updateVarNum, false);
}

function updateVarNum(event) {
     variables = document.getElementById("inputNum").value;
  var lblInput = document.getElementById("lblInput");
  lblInput.innerHTML = "";
  showUserInputs(variables);
}

function showUserInputs(variables) {
  var lblInput = document.getElementById("lblInput");
  var opTxt = ["OR, or, |, ||", "AND, and, &, &&", "XOR, xor"];
  var opVal = ["||", "&&", "^"];

  for(i = 1; i <= parseInt(variables); i++) {
    if (i != 1) {
      var formGroup = document.createElement("div");
          formGroup.className = "form-group";                   
      var selectLabel = document.createElement("label");
          selectLabel.for = "input" + (i-1);
          selectLabel.className = "col-md-4";
          selectLabel.innerHTML = "Operator " + (i-1) + ":";
          formGroup.appendChild(selectLabel);
      var inputName = document.createElement("select");
          inputName.className = "col-md-7 form-control";
          inputName.id = "input" + (i-1);
          inputName.name = "input" + (i-1);
          inputName.addEventListener("change", updatePredicate, false);
          createDrop(inputName , opTxt, opVal);
          formGroup.appendChild(inputName);
      lblInput.appendChild(formGroup);
    }               
    var formGroup = document.createElement("div");
        formGroup.className = "form-group";
    var inputLabel = document.createElement("label");
        inputLabel.for = "var" + i;
        inputLabel.className = "col-md-4";
        inputLabel.innerHTML = "Variable " + i + ":";
        // inputLabel.style.border = "solid";
        formGroup.appendChild(inputLabel);
    var varName = document.createElement("input");
        varName.type = "text";
        varName.className = "col-md-7 form-control";
        varName.id = "var" + i;
        varName.name = "var" + i;
        // varName.value = document.getElementById("var" + i);
        varName.addEventListener("change", updatePredicate, false);
        // varName.style.border = "solid";
        formGroup.appendChild(varName);
    lblInput.appendChild(formGroup);
  }
}

function createDrop (inputElement, arr1, arr2) {
  var option = document.createElement("option");
  option.text = "-- Pick Op --";
  option.value = "";
  option.selected = "selected";
  option.disabled = "disabled";
  inputElement.appendChild(option);
  for (var i = 0; i < 4; i++) {
      option = document.createElement("option");
      option.text = arr1[i];
      option.value = arr2[i];
      inputElement.appendChild(option);
  }
}

function updatePredicate(event) {
  predicateTxt = document.getElementById("predicateTxt");
  predicateTxt.innerHTML = "";
  predicate = "";
  for(i = 1; i <= parseInt(variables); i++) {
    if (i != 1) {
      predicate += document.getElementById("input" + (i-1)).value + "  ";
    }
    predicate += document.getElementById("var" + i).value + "  ";
  }
  predicateTxt.innerHTML = predicate;
}

function validateForm() {
  var j;
  for (j = 1; j <= parseInt(variables); j++) {
    var regex = /^\!?[a-zA-Z]{1,15}[0-9]{0,3}$/;
    var ctrl =  document.getElementById("var" + j).value.trim();
    var testing = regex.test(ctrl);
    if (testing == false) {
      alert("Invalid name for Variable" + j);
      return false;
    }
  }
  return true;
}