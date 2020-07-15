var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
var yearStart = 2010;
var yearEnd = 2040;
var month = 0;
var year = 0;

function loadCalendarMonths() {
    for (var i = 0; i < months.length; i++) {
        var doc = document.createElement("div");
        doc.innerHTML = months[i];
        doc.classList.add("dropdown-item");

        doc.onclick = (function () {
            var selectedMonth = i;
            return function () {
                month = selectedMonth;
                document.getElementById("curMonth").innerHTML = months[month];
                loadCalendarDays();
                return month;
            }
        })();

        document.getElementById("months").appendChild(doc);
    }
}

function loadCalendarYears() {
    document.getElementById("years").innerHTML = "";

    for (var i = yearStart; i <= yearEnd; i++) {
        var doc = document.createElement("div");
        doc.innerHTML = i;
        doc.classList.add("dropdown-item");

        doc.onclick = (function () {
            var selectedYear = i;
            return function () {
                year = selectedYear;
                document.getElementById("curYear").innerHTML = year;
                loadCalendarDays();
                return year;
            }
        })();

        document.getElementById("years").appendChild(doc);
    }
}

function loadCalendarDays() {
    document.getElementById("calendarDays").innerHTML = "";

    var tmpDate = new Date(year, month, 0);
    var num = daysInMonth(month, year);
    var dayofweek = tmpDate.getDay();       
    // find where to start calendar day of week

    for (var i = 0; i <= dayofweek; i++) {
        var d = document.createElement("div");
        d.classList.add("day");
        d.classList.add("blank");
        document.getElementById("calendarDays").appendChild(d);
    }

    for (var i = 0; i < num; i++) {
        var tmp = i + 1;
        var d = document.createElement("div");
        d.id = "calendarday_" + i;
        d.className = "day";
        d.innerHTML = tmp;

        document.getElementById("calendarDays").appendChild(d);
    }

    var clear = document.createElement("div");
    clear.className = "clear";
    document.getElementById("calendarDays").appendChild(clear);
}

function daysInMonth(month, year)
{
    var d = new Date(year, month+1, 0);
    return d.getDate();
}

window.addEventListener('load', function () {
    var date = new Date();
    month = date.getMonth();
    year = date.getFullYear();
    document.getElementById("curMonth").innerHTML = months[month];
    document.getElementById("curYear").innerHTML = year;
    loadCalendarMonths();
    loadCalendarYears();
    loadCalendarDays();
});
            