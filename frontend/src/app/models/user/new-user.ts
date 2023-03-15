export class NewUser {
    
    username: string
    password: string
    name: string
    lastName: string
    email: string
    country: string

    constructor(username: string, password: string, name: string, lastName: string, email: string, country: string) {
        this.username = username
        this.password = password
        this.name = name
        this.lastName = lastName
        this.email = email
        this.country = country
    }
}
