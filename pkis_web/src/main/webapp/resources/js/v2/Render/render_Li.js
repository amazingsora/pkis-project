function NewLi(obj, itemType) {
    var li = undefined;

    if (obj._ref && obj._ref.indexOf("TopButton") > -1 && itemType === "li") {
        var a = document.createElement("a");
        a.href = `#${obj.displayname}`;
        a.setAttribute("id", "a_" + obj.id);
        a.setAttribute("data-toggle", "tab");
        a.setAttribute("onclick", obj._js.replace(/onclick/, ''));
        if(obj.displayname.indexOf("_") > -1){
			a.text = obj.displayname.replaceAll("_", " ");
			if(obj.displayname.match(/13./g))
				a.text = a.text + "供應商服務評估表";
			else if(obj.displayname.match(/10./g))
				a.text = "10.DC Fees(Distribution Center Fees)物流費用";
		}else if(obj.displayname === "審核評估"){
			a.setAttribute("style", "background: red;");
			a.text = obj.displayname;
		}else{
			a.text = obj.displayname;
			if(obj.displayname.match(/10./g))
				a.text = "10.DC Fees(Distribution Center Fees)物流費用";
		}

        li = document.createElement("li");
        li.setAttribute("id", "li_" + obj.id);
        if (dataType === obj.displayname) li.setAttribute("class", "active");
        li.appendChild(a);
    }

    return li;
}