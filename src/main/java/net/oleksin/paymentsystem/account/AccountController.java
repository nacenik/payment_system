package net.oleksin.paymentsystem.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.person.PersonProvider;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Account Controller", description = "Get all person's accounts")
@RestController
@RequestMapping(value = "/api/persons",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@AllArgsConstructor
public class AccountController {

    private final PersonProvider personProvider;
    private final Converter<AccountDto, AccountDto, Account> accountConverter;

    @Operation(summary = "Get accounts by person id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Getting all person's accounts",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AccountDto.class))),
                            @Content(
                                    mediaType = "application/xml",
                                    array = @ArraySchema(schema = @Schema(implementation = AccountDto.class))),
                    })
    })
    @PreAuthorize("@authPermissionComponent.isAdminOrBelongToBankAccount(principal, #id)")
    @GetMapping(value = "{id}/accounts")
    public List<AccountDto> getAccountsByPersonId(@Parameter(description = "Person id") @PathVariable("id") Long id) {
        return personProvider.getAccountsByPersonId(id).stream()
                .map(accountConverter::toResponseDto)
                .collect(Collectors.toList());
    }
}
