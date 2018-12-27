package com.juyoufuli.jd_address_select;

import java.util.List;

/**
 * @Author : dongfang
 * @Created Time : 2018-12-26  16:25
 * @Description:
 */
public class CityBean {

    /**
     * name : 北京
     * city : [{"name":"北京","area":["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]}]
     */

    private String name;
    private List<CityBeanInner> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBeanInner> getCity() {
        return city;
    }

    public void setCity(List<CityBeanInner> city) {
        this.city = city;
    }

    public static class CityBeanInner {
        /**
         * name : 北京
         * area : ["东城区","西城区","崇文区","宣武区","朝阳区","丰台区","石景山区","海淀区","门头沟区","房山区","通州区","顺义区","昌平区","大兴区","平谷区","怀柔区","密云县","延庆县"]
         */

        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }
    }
}
