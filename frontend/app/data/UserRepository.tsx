export async function getUsers(currentPage: number, pageSize: number): Promise<Response> {
    return fetch(`http://localhost:8080/app/users?page=${currentPage}&size=${pageSize}`)
        .then((response) => response.json())
        .catch((error) => {
            throw new Error("Error fetching users:", error)
        });
}
