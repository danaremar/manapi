import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { MyUser } from "src/app/models/user/my-user";
import { UserUpdate } from "src/app/models/user/update-user";
import { environment } from "src/environments/environment";

const PROFILE_IMAGE_KEY = 'UserProfileImage'

@Injectable({
    providedIn: 'root'
})
export class UserService {

    // URL
    hostUrl = environment.backendEndpoint + '/profile/'

    // Profile image Url
    imageUrl: string | null | undefined

    // CONSTRUCTOR
    constructor(private httpClient: HttpClient) { }

    /*
    *
    * OPERATIONS
    * 
    */

    // GET
    public getMyProfile(): Observable<any> {
        let url = this.hostUrl
        return this.httpClient.get<MyUser>(url)
    }

    // UPDATE
    public updateProfile(userUpdate: UserUpdate): Observable<any> {
        let url = this.hostUrl
        return this.httpClient.put<UserUpdate>(url, userUpdate)
    }

    // DELETE
    public deleteUserProfile(): Observable<any> {
        let url = this.hostUrl
        return this.httpClient.delete<any>(url)
    }

    /*
    *
    * PROFILE IMAGE OPERATIONS
    * 
    */

    public getUrlFromProfile(image: string | null): string | null {
        // not exists
        if (image == null || image == '') {
            return null

            // URL from external site
        } else if (image.includes('/')) {
            return image

            // URL from backend
        } else {
            return environment.backendEndpoint + '/images/' + image
        }
    }

    // SET LOCAL
    public setImage(image: string | null): void {
        this.imageUrl = this.getUrlFromProfile(image)
    }

    // RELOAD
    public reloadProfileImage(): void {
        this.getMyProfile().subscribe({
            next: (n) => {
                let profileImageUid = n.imageUid
                this.setImage(profileImageUid ? profileImageUid : null)
            },
            error: (e) => {
                this.setImage(null)
            }
        })
    }

    // UPLOAD REMOTE
    public uploadUserImageProfile(profileImage: File): Observable<any> {
        let url = this.hostUrl + 'image'
        const formData: FormData = new FormData()
        formData.append('image', profileImage)
        return this.httpClient.post<any>(url, formData)
    }

    // DELETE REMOTE
    public deleteUserImageProfile(): Observable<any> {
        let url = this.hostUrl + 'image'
        return this.httpClient.delete<any>(url)
    }

}