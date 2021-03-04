function GenDiv(processCode, data, dom) {
    if (processCode === "GenDiv")
        return genDiv(data, dom);
    else if (processCode === "GenDiv_Readonly")
        return genDiv_Readonly(data, dom);
    else
        return false;
}