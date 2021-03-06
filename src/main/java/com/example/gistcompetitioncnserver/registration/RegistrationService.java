package com.example.gistcompetitioncnserver.registration;

import com.example.gistcompetitioncnserver.registration.emailsender.EmailSender;
import com.example.gistcompetitioncnserver.registration.token.EmailConfirmationToken;
import com.example.gistcompetitioncnserver.registration.token.EmailConfirmationTokenService;
import com.example.gistcompetitioncnserver.user.User;
import com.example.gistcompetitioncnserver.user.UserRole;
import com.example.gistcompetitioncnserver.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request, HttpServletRequest urlRequest){
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
        );

        String baseUrl = urlRequest.getRequestURL().toString();
        String link = baseUrl.substring(0, baseUrl.length() - 12 ) + "confirm?token=" + userService.signUpUser(user); // redirect home page in local

        emailSender.send(
                request.getEmail(),
                buildEmail(request.getUsername(), link));
        return user.getId().toString();
    }

    public String resendEmail(User user, HttpServletRequest urlRequest){

        Optional<EmailConfirmationToken> userEmailToken = emailConfirmationTokenService.findEmailTokenByUserId(user.getId()) ;
        emailConfirmationTokenService.deleteToken(userEmailToken.get().getToken()); // delete previous token

        String token = userService.createToken(user);
        String baseUrl = urlRequest.getRequestURL().toString();
        String link = baseUrl.substring(0, baseUrl.length() - 12 ) + "confirm?token=" + token; // redirect home page in local

        emailSender.send(
                user.getEmail(),
                buildEmail(user.getUsername(), link));
        return "email is resent"; // redirect??? ?????? -> ???????????? ??????????????? ????????? alert??? ?????? ??? ?????????
    }



    @Transactional
    public String confirmToken(String token, String email) {
        emailConfirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(email);

        return "https://gist-petition-web-qtmha8hh2-betterit.vercel.app"; // redirect to the page
    }

    private String buildEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>???????????????</title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <style>\n" +
                "        .title{\n" +
                "            padding: 20px 0px; \n" +
                "            border-bottom: 1px solid rgb(223, 223, 223); \n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        .table-overall{\n" +
                "            font-size: 16px; \n" +
                "            color: rgb(34, 34, 34); \n" +
                "            line-height: 1.2;\n" +
                "        }\n" +
                "        .line-table{\n" +
                "            height: 1px; \n" +
                "            background: black; \n" +
                "            position: relative;\n" +
                "        }\n" +
                "        .line-content{\n" +
                "            width: 20%; \n" +
                "            height: 1px;    \n" +
                "        }\n" +
                "        .content{\n" +
                "            font-size: 16px;\n" +
                "            padding:10px;\n" +
                "            line-height: 160%;\n" +
                "        }    \n" +
                "        .footer{\n" +
                "            font-size: 13px; \n" +
                "            line-height: 26px; \n" +
                "            padding:15px; \n" +
                "            padding-top:0px;\n" +
                "        }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "      <div>\n" +
                "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family: NanumSquare, sans-serif;\">\n" +
                "            <tbody><tr>\n" +
                "                <td align=\"center\" style=\"padding: 25px 18px;\">\n" +
                "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"400px\" class=\"table-overall\">\n" +
                "                    <tbody>\n" +
                "                        <tr>\n" +
                "                            <td align=\"center\" style=\"padding: 25px 0px;\">\n" +
                "                                <img src=\"https://www.gist.ac.kr/en/img/sub01/01030301_logo.jpg\" \n" +
                "                                    style=\"display: block; border: 0px; width:200px\"\n" +
                "                                />\n" +
                "                            </td>\n" +
                "                            </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"line-table\">\n" +
                "                                <div class=\"line-content\"></div>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td align=\"center\" class=\"title\" >\n" +
                "                                <b style=\"color: rgb(255, 52, 0);\">????????? ??????</b>??? ????????? ?????????\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td class=\"line-table\">\n" +
                "                                <div class=\"line-content\"></div>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    \n" +
                "                        <tr>\n" +
                "                            <td style=\"line-height: 20px; padding: 23px 0px;\">\n" +
                "                                <p class=\"content\" style=\" margin: 0px 0px 20px;\">\n" +
                "                                    ??????????????? " + name + "???,<br>\n" +
                "                                    ShouTInG ???????????? ????????? ?????? ????????? ?????????????????????.\n" +
                "                                </p>\n" +
                "                                <p class=\"content\" style=\"margin: 0px; padding-top:0; \">\n" +
                "                                    ShouTInG ??????????????? ?????? ????????? ????????? ???????????????.\n" +
                "                                    <br>\n" +
                "                                    <span style=\"color: rgb(255, 52, 0);\">\n" +
                "                                        ?????? ????????? ???????????? ????????? ????????? ????????? ?????????.\n" +
                "                                    </span> \n" +
                "                                    <br>\n" +
                "                                    <div style=\"padding:10px\">\n" +
                "                                        <a href="+ link + ">????????? ??????????????? ????????? ???????????????</a>\n" +
                "                                    </div>\n" +
                "                                    <div style=\"padding:10px; line-height: 160%;\">\n" +
                "                                        ????????? ????????? ????????? ????????? ???????????? 15??? ?????? ???????????????.\n" +
                "                                    </div>\n" +
                "                                </p>\n" +
                "                            </td>\n" +
                "                        </tr> \n" +
                "                        <tr>\n" +
                "                            <td class=\"line-table\">\n" +
                "                                <div class=\"line-content\"></div>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td style=\"padding-top: 25px; padding-bottom: 24px; border-top: 1px solid black;\">\n" +
                "                                <div class=\"footer\">\n" +
                "                                    ??? ????????? ?????????????????????. ??? ????????? ????????? ShouTInG ???????????????\n" +
                "                                    <br> ?????????????????? ????????????.\n" +
                "                                </div>\n" +
                "                                <div><img\n" +
                "                                    src=\"https://giai.gist.ac.kr/thumbnail/popupzoneDetail/PHO_201908061057017980.jpg\"\n" +
                "                                    style=\"display: block; border: 0px; width: 150px\"\n" +
                "                                />   \n" +
                "                                </div>     \n" +
                "                            </td>\n" +
                "                        </tr></tbody>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr></tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }



}
