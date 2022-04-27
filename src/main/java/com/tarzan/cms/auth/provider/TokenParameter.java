package com.tarzan.cms.auth.provider;

import com.tarzan.cms.auth.support.Kv;
import lombok.Data;

/**
 * TokenParameter
 *
 * @author tarzan 
 */
@Data
public class TokenParameter {

    private Kv args = Kv.create();

}
