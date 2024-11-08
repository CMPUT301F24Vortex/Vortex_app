/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendInvitationNotification = functions.firestore
    .document("invitations/{invitationId}")
    .onUpdate((change, context) => {
        const newValue = change.after.data();
        const previousValue = change.before.data();

        // Only proceed if the status has changed
        if (newValue.status === previousValue.status) {
            return null;
        }

        const status = newValue.status;
        const invitationId = context.params.invitationId;
        const fcmToken = newValue.fcmToken; // Assume this is saved in each invitation document

        let notificationPayload;

        // Add a `type` field in `data` to differentiate between winning and losing notifications
        if (status === "win") {
            notificationPayload = {
                notification: {
                    title: "Congratulations!",
                    body: "You've won the invitation. Please accept or decline.",
                },
                data: {
                    type: "win", // <-- Type for winning
                    invitationId: invitationId,
                },
            };
        } else if (status === "lose") {
            notificationPayload = {
                notification: {
                    title: "Invitation Update",
                    body: "You're currently on the waiting list. Stay or leave?",
                },
                data: {
                    type: "lose", // <-- Type for losing
                    invitationId: invitationId,
                },
            };
        } else {
            return null; // No notification if status is neither "win" nor "lose"
        }

        // Send FCM message
        return admin.messaging().sendToDevice(fcmToken, notificationPayload)
            .then(response => {
                console.log("Notification sent successfully:", response);
                return null;
            })
            .catch(error => {
                console.error("Error sending notification:", error);
            });
    });
