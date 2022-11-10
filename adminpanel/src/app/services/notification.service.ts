import {Injectable, NgZone} from '@angular/core';
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    public snackbar: MatSnackBar,
    private zone: NgZone,
  ) { }

  send(panelClass: [string], message: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = panelClass;
    config.duration = 3000;
    config.verticalPosition = 'top';
    config.horizontalPosition = 'center';
    this.zone.run(() => {
      this.snackbar.open(message, 'Ok', config);
    });
  }
}
