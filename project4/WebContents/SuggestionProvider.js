
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function SuggestionProvider() {
    this.states = [
        "Alabama", "Alaska", "Arizona", "Arkansas",
        "California", "Colorado", "Connecticut",
        "Delaware", "Florida", "Georgia", "Hawaii",
        "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana",
        "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", 
        "Mississippi", "Missouri", "Montana",
        "Nebraska", "Nevada", "New Hampshire", "New Mexico", "New York",
        "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", 
        "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", 
        "Washington", "West Virginia", "Wisconsin", "Wyoming"  
    ];
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    
    var that = oAutoSuggestControl;
	var aSuggestions = [];
	var sTextboxValue = oAutoSuggestControl.textbox.value;

	// search for suggestions if input text box is non-empty
	if (sTextboxValue.length > 0) {
		var xmlHttp = new XMLHttpRequest();
		var request = "suggest?query=" + encodeURI(sTextboxValue);
		xmlHttp.open("GET", request);
		xmlHttp.onreadystatechance = function() {
			if (xmlHttp.readyState == 4) {
				var response = xmlHttp.responseText;
				var xmlDom = new DOMParser().parseFromString(response, "text/xml");
				var suggestions = xmlDom.getElementsByTagName("suggestion");
				var currSuggestion;
				for (var i = 0; i < suggestions.length; i++) {
					currSuggestion = suggestions[i].getAttribute("data");
					aSuggestions.push(currSuggestion);
				}

				oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
			}
		};

		xmlHttp.send(null);
	};
};