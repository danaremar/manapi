export class UserUpdate {

    constructor(
        username: string,
        name: string,
        lastName: string,
        email: string,
        country: string,
        oldPassword: string,
        newPassword: string
    ) {
        this.username = username
        this.name = name
        this.lastName = lastName
        this.email = email
        this.country = country
        this.oldPassword = oldPassword
        this.newPassword = newPassword
    }

    username: string
    name: string
    lastName: string
    email: string
    country: string
    oldPassword: string
    newPassword: string
}