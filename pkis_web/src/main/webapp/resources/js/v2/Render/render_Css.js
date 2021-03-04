function genderClassName(indent, uiType) {
    var _val = `${uiType}-${indent} `;

    switch (uiType) {
    case "div-clear":
        _val = " clr-style";
        break;
    case "dis-flex":
        _val = " dis-flex";
        break;
    case "button":
        _val += "btn btn-dark btn-fw";
        break;
    case "label":
        _val += "col-md-3";
        break;
    case "text":
    case "date":
        _val += "form-control";
        break;
    case "dropdownlist":
        _val += "dropdown-toggle dropdown-list";
        break;
    //case "dropdownlist_with_checkbox":
    //    _val += "btn dropdown-toggle btn-select dropdown-list";
    //    break;
    case "radio":
    case "checkbox":
        _val += "form-inline " + uiType;
        break;
    case "render_Labelfor":
        _val = "dis-flex";
        break;
    case "btn-submit":
        _val = "btn skh-btn-primary";
        break;
    default:
        break;
    }

    return _val;
}

function get_element_css(param = "", type = "", vh = "") {
    var css;
    if (param === "label") {
        switch (type) {
            case "":
                css = `class="col-md-9 col-form-label" `;
                break;
            case "implement":
                css = `class="col-md-3 col-form-label" `;
                break;
            case "child":
                css = `class="col-md-6 col-form-label" `;
                break;
        }
    } else if (param === "text") {
        switch (type) {
            case "":
                css = `class="form-control" `;
                break;
            case "label":
                css = `class="col-md-3 col-form-label col-ta-right" `;
                break;
            case "span":
                css = `class="input-group-addon bg-teal-costomer"`;
                break;
        }
    } else if (param === "button") {
        switch (type) {
            case "add":
                css = `class="btn btn-primary sendadd" `;
                break;
            case "back":
                css = `"btn btn-primary btn-back-to-list" `;
                break;
            case "else":
                css = `class="btn btn-primary" `;
                break;
        }

    } else if (param === "dropdown") {
        switch (type) {
            case "":
                css = `class="show-tick" data-plugin="selectpicker" `;
                break;
            case "label":
                css = `class="col-md-3 col-form-label col-ta-right" `;
                break;
        }
    } else if (param === "radio") {
        switch (type) {
            case "div":
                css = `class="radio-custom radio-primary${vh === "h" ? " radio-inline" : ""}" `;
                break;
            case "label":
                css = `class="col-md-3 col-form-label col-ta-right" `;
                break;
        }
    } else if (param === "checkbox") {
        switch (type) {
            case "div":
                css = `class="checkbox-custom checkbox-primary${vh === "h" ? " checkbox-inline" : ""}" `;
                break;
            case "label":
                css = `class="col-md-3 col-form-label col-ta-right" `;
                break;
        }
    } else if (param === "child") {
        switch (type) {
            case "label":
                css = `class="col-md-3 col-form-label" `;
                break;
        }
    } else {
        css = `class="col-md-6"`;
    }

    return css;
}