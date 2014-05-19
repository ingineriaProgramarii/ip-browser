window.console = window.console || {
    "log": function() {},
    "error": function() {}
};

/* IE Node.textContent() alternative */

function textValue(element) {
    var collector = [];
    textValueCollector(element, collector);
    return collector.join("");
}

function textValueCollector(element, collector) {
    var node;
    for (node = element.firstChild; node; node = node.nextSibling)      {
        switch (node.nodeType) {
            case 3: // text
            case 4: // cdata
                collector.push(node.nodeValue);
                break;
            case 8: // comment
                break;
            case 1: // element
                if (node.tagName == 'SCRIPT') {
                    break;
                }
                // FALL THROUGH TO DEFAULT
            default:
                // Descend
                textValueCollector(node, collector);
                break;
        }
    }
}