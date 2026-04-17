(function () {
    var API_BASE_URL = "http://localhost:8080";

    async function postJson(path, payload) {
        var response = await fetch(API_BASE_URL + path, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        var data = null;
        try {
            data = await response.json();
        } catch (error) {
            data = null;
        }

        if (!response.ok) {
            var message = data && data.message ? data.message : "Request failed";
            throw new Error(message);
        }

        return data;
    }

    async function getJson(path) {
        var response = await fetch(API_BASE_URL + path, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            var data = null;
            try {
                data = await response.json();
            } catch (error) {
                data = null;
            }
            var message = data && data.message ? data.message : "Request failed";
            throw new Error(message);
        }

        return response.json();
    }

    window.UserApi = {
        login: function (email, password) {
            return postJson("/users/login", {
                email: email,
                password: password
            });
        },
        register: function (name, email, password) {
            return postJson("/users/register", {
                name: name,
                email: email,
                password: password
            });
        }
    };

    window.BudgetApi = {
        setBudget: function (userId, amount) {
            return postJson("/budget/set", {
                userId: Number(userId),
                amount: Number(amount),
                period: "monthly"
            });
        },
        getBudget: function (userId) {
            return getJson("/budget/" + encodeURIComponent(userId));
        }
    };
})();