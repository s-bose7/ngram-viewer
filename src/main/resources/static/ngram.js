function createPlot(data) {
    // Clear any previous chart
    d3.select("#plot").html("");

    // Set the chart’s dimensions
    const width = 1175;
    const height = 489;
    const marginTop = 80;
    const marginRight = 50;
    const marginBottom = 30;
    const marginLeft = 50;

    // Create the positional scales
    const x = d3.scaleUtc()
        .domain(d3.extent(data, d => new Date(d.year, 0, 1)))
        .range([marginLeft, width - marginRight]);

    const y = d3.scaleLinear()
        .domain([0, d3.max(data, d => d.value)]).nice()
        .range([height - marginBottom, marginTop]);

    // Create the SVG container
    const svg = d3.select("#plot")
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("viewBox", [0, 0, width, height])
        .attr("style", "max-width: 100%; height: auto; overflow: visible; font: 10px sans-serif;");

    // Add the horizontal axis
    let xAxis = svg.append("g")
        .attr("transform", `translate(0,${height - marginBottom})`)
        .call(d3.axisBottom(x).ticks(width / 80).tickSizeOuter(0));

    xAxis.selectAll("text") 
        .style("font-size", "12px");
    
        // Add the vertical axis
    let yAxis = svg.append("g")
        .attr("transform", `translate(${marginLeft},0)`)
        .call(d3.axisLeft(y))
        .call(g => g.select(".domain").remove())
        .call(g => g.append("text")
            .attr("x", -marginLeft)
            .attr("y", 10)
            .attr("fill", "currentColor")
            .attr("text-anchor", "start"));

    yAxis.selectAll("text") 
        .style("font-size", "12px");
    
    const yTicks = y.ticks();
    const gridLinesGroup = svg.append("g")
        .attr("class", "grid-lines");
    
    gridLinesGroup.selectAll(".grid-line")
        .data(yTicks)
        .enter().append("line")
        .attr("class", "grid-line")
        .attr("x1", 0) 
        .attr("x2", width) 
        .attr("y1", d => y(d))
        .attr("y2", d => y(d));
    
    // Compute the points in pixel space as [x, y, z], where z is the name of the series.
    const points = data.map((d) => [x(new Date(d.year, 0, 1)), y(d.value), d.word]);

    // Group the points by series.
    const groups = d3.rollup(points, v => Object.assign(v, {z: v[0][2]}), d => d[2]);

    // Create a color scale
    const color = d3.scaleOrdinal(d3.schemeCategory10)
        .domain(Array.from(groups.keys()));

    // Create a line generator
    const line = d3.line()
        .x(d => d[0])
        .y(d => d[1]);

    // Draw the lines
    const path = svg.append("g")
        .attr("fill", "none")
        .attr("stroke-width", 2.5)
        .attr("stroke-linejoin", "round")
        .attr("stroke-linecap", "round")
        .selectAll("path")
        .data(groups.values())
        .join("path")
        .style("mix-blend-mode", "multiply")
        .attr("stroke", d => color(d.z))
        .attr("d", line);
    

    // Add an invisible layer for the interactive tip.
    const dot = svg.append("g")
        .attr("display", "none");

    dot.append("circle")
        .attr("r", 2.5);

    dot.append("text")
        .attr("text-anchor", "middle")
        .attr("y", -8)
        .style("font-size", "15px");

    svg
        .on("pointerenter", pointerentered)
        .on("pointermove", pointermoved)
        .on("pointerleave", pointerleft)
        .on("touchstart", event => event.preventDefault());

    return svg.node();

    // When the pointer moves, find the closest point, update the interactive tip, and highlight
    // the corresponding line. Note: we don't actually use Voronoi here, since an exhaustive search
    // is fast enough.
    function pointermoved(event) {
        const [xm, ym] = d3.pointer(event);
        const i = d3.leastIndex(points, ([x, y]) => Math.hypot(x - xm, y - ym));
        const [x, y, k] = points[i];
        path.style("stroke", ({z}) => z === k ? null : "#ddd").filter(({z}) => z === k).raise();
        dot.attr("transform", `translate(${x},${y})`);
        dot.select("text").text(k);
        svg.property("value", data[i]).dispatch("input", {bubbles: true});
    }

    function pointerentered() {
        path.style("mix-blend-mode", null).style("stroke", "#ddd");
        dot.attr("display", null);
    }

    function pointerleft() {
        path.style("mix-blend-mode", "multiply").style("stroke", null);
        dot.attr("display", "none");
        svg.node().value = null;
        svg.dispatch("input", {bubbles: true});
    }
}

$(function() {
    var host = 'http://localhost:4567';
    const history_server = host + '/history';

    $('#history').click(historyButton);

    function get_params() {
        return {
            words: document.getElementById('words').value,
            startYear: document.getElementById('startYear').value,
            endYear: document.getElementById('endYear').value,
        }
    }

    function historyButton() {
        $("#plot").show();
        var params = get_params();
        $.get({
            async: false,
            url: history_server,
            data: params,
            success: function(data) {
                console.log(data);
                createPlot(data);
            },
            error: function(xhr, status, error) {
                console.error("Error:", status, error);
                console.error("Response Text:", xhr.responseText);
            },
            dataType: 'json'
        }); 
    }
});
