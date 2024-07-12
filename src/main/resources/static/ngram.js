$(function() {
	plot = document.getElementById('plot');
	textresult = document.getElementById('textresult');

	var host;

    host = 'http://localhost:4567';
    const history_server = host + '/history';
    const historytext_server = host + '/historytext';

    function get_params() {
        return {
            // corpora: document.getElementById('corpora').value,
            words: document.getElementById('words').value,
            startYear: document.getElementById('startYear').value,
            endYear: document.getElementById('endYear').value,
        }
    }

    $('#history').click(historyButton);

    function historyButton() {
        $("#textresult").hide();
        $("#plot").show();

        var params = get_params();
        console.log(params);
        $.get({
            async: false,
            url: history_server,
            data: params,
            success: function(data) {
            	console.log(data)

                plot.src = 'data:image/png;base64,' + data;

            },
            error: function(data) {
            	console.log("error")
            	console.log(data);
            	plot.src = 'data:image/png;base64,' + data;
            },
            dataType: 'json'
        });
    }
});